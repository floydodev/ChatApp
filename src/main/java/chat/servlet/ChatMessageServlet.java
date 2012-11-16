package chat.servlet;

//import static chat.beanFactory.BeanFactory._ChannelUserManager;
//import static chat.beanFactory.BeanFactory._MessengerService;
//import static chat.beanFactory.BeanFactory.getInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.support.WebApplicationContextUtils;

import chat.model.ChatMessage;
import chat.model.User;
import chat.service.ChannelUserManager;
import chat.service.SnapshotMessengerService;

public class ChatMessageServlet extends HttpServlet implements CometProcessor {
															//, HttpRequestHandler {
	static private final Log log = LogFactory.getLog(ChatMessageServlet.class);
	
	private List<HttpServletResponse> userConnections = new ArrayList<HttpServletResponse>();
	private SnapshotMessengerService messageSender = null;
	private ChannelUserManager channelUserManager = null;
	Thread messageSenderThread = null;
	
	public void init() throws ServletException {
		channelUserManager = WebApplicationContextUtils.getRequiredWebApplicationContext(
									getServletContext()).getBean("channelUserManager", ChannelUserManager.class);
		messageSender = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("messengerService", SnapshotMessengerService.class);

		messageSenderThread = 
			new Thread(messageSender, "MessageSender[" + getServletContext().getContextPath() + "]");
		messageSenderThread.setDaemon(true);
		messageSenderThread.start();
	}

	@Override
	public void destroy() {
		userConnections.clear();
		messageSender.stop();
		try {
			long waitLengthSecs = 10;
			log.info("Wait for thread to stop (" + waitLengthSecs + " secs)");
			messageSenderThread.join(waitLengthSecs * 1000);
			log.info("Thread stopped");
		} catch (InterruptedException e) {
			log.error("Caught InterruptedException - why didn't it stop?" + e);
			//e.printStackTrace();
		}
		messageSender = null;
	}
	
	public void event(CometEvent event) throws IOException, ServletException {
		switch (event.getEventType()) {
			case BEGIN :
			case END :
			case ERROR : {
				handleConnectionLifecyle(event);
				break;
			}
			case READ : {
				processClientActionRequest(event);
				break;
			}
			default: {
				System.out.println("Got some other event type: " + event.getEventType());
			}
		}

	}

	private void handleConnectionLifecyle(CometEvent event)	throws IOException, ServletException {
		
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();
		String userEmailAddress = (String)request.getSession(true).getAttribute("user");
		
		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			// Starts the long-polling cycle
			
			log.info("Begin for session: " + request.getSession(true).getId() + ", user=" + userEmailAddress);
			event.setTimeout(900*1000*1000); /* timeout is 15 minutes */
			synchronized(channelUserManager) {
				channelUserManager.addUserConnection(channelUserManager.getUser(userEmailAddress), response);
				//channelUserManager.getUser(userEmailAddress).setConnection(response);
			}
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			log.info("Error for session: " + request.getSession(true).getId() + ", user=" + userEmailAddress);
			synchronized(channelUserManager) {
				channelUserManager.removeUserConnection(channelUserManager.getUser(userEmailAddress));
				//channelUserManager.getUser(userEmailAddress).setConnection(null);
				// set to null. Only necessary if we are looping through each user checking the connections..
				// before deciding whether to send message or not
			}
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.END) {
			// Completes a long-polling cycle. The client is designed to start another cycle
			log.info("End for session: " + request.getSession(true).getId() + ", user=" + userEmailAddress);
			synchronized(channelUserManager) {
				channelUserManager.removeUserConnection(channelUserManager.getUser(userEmailAddress));
				//channelUserManager.getUser(userEmailAddress).setConnection(null);
			}
			event.close();
		}
	}

	private void processClientActionRequest(CometEvent event) throws IOException {
		HttpServletRequest request = event.getHttpServletRequest();
		String action = request.getParameter("action");

		
		if (ChatClientAction.SEND.equals(action)) {
			log.info("Handle chat request");
			String chatMessageStr = request.getParameter("chatMessage");
			if (chatMessageStr != null && !"".equals(chatMessageStr)) {
				log.info("chatMessageStr=" + chatMessageStr);
				String userEmailAddress = (String)request.getSession(true).getAttribute("user");
				User user = channelUserManager.getUser(userEmailAddress);
				ChatMessage chatMessage = 
						new ChatMessage(chatMessageStr, Calendar.getInstance().getTime(), user, messageId++);
				messageSender.receive(chatMessage);
			} else {
				log.info("chatMessageStr=<blank>");
			}
		}
		else if (ChatClientAction.POLL.equals(action)) {
			log.info("Handle polling request");
			String lastMessageIdStr = request.getParameter("lastMessageId");
			if (lastMessageIdStr != null && !"".equals(lastMessageIdStr)) {
				int lastMessageId = Integer.parseInt(lastMessageIdStr);
				String userEmailAddress = (String)request.getSession(true).getAttribute("user");
				User user = channelUserManager.getUser(userEmailAddress);
				user.setLastMessageId(lastMessageId);
				if (lastMessageId == -1) {
					messageSender.snapshot();
				}
			}
		}
	}
	
	private static int messageId = 1;
//
//	public void handleRequest(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		
//	}

}
