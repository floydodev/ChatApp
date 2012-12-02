package chat.multichannel.servlet;

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
import org.springframework.web.context.support.WebApplicationContextUtils;

import chat.multichannel.model.ChatMessage;
import chat.multichannel.model.User;
import chat.multichannel.service.ChannelUserManager;
import chat.multichannel.service.MessengerService;
import chat.multichannel.service.UserConnectionManager;
import chat.util.ChatClientAction;

public class ChatMessageServlet extends HttpServlet implements CometProcessor {

	static private final Log log = LogFactory.getLog(ChatMessageServlet.class);
	
	private List<HttpServletResponse> userConnections = new ArrayList<HttpServletResponse>();
	private ChannelUserManager channelUserManager = null;
	private UserConnectionManager userConnectionManager = null;
	private MessengerService messageSender = null;
	private Thread messageSenderThread = null;
	
	public void init() throws ServletException {
		channelUserManager = WebApplicationContextUtils.getRequiredWebApplicationContext(
									getServletContext()).getBean("channelUserManager", ChannelUserManager.class);
		userConnectionManager = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("userConnectionManager", UserConnectionManager.class);
		messageSender = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("messengerService", MessengerService.class);

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
			
			log.info("user=" + userEmailAddress + " - Begin for session: " + request.getSession(true).getId());
			event.setTimeout(900*1000*1000); /* timeout is 15 minutes */
			synchronized(userConnectionManager) {
				userConnectionManager.addUserConnection(channelUserManager.getUser(userEmailAddress), response);
				//channelUserManager.getUser(userEmailAddress).setConnection(response);
			}
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			log.info("user=" + userEmailAddress + " - Error for session: " + request.getSession(true).getId());
			synchronized(userConnectionManager) {
				userConnectionManager.removeUserConnection(channelUserManager.getUser(userEmailAddress));
				//channelUserManager.getUser(userEmailAddress).setConnection(null);
				// set to null. Only necessary if we are looping through each user checking the connections..
				// before deciding whether to send message or not
			}
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.END) {
			// Completes a long-polling cycle. The client is designed to start another cycle
			log.info("user=" + userEmailAddress + " - End for session: " + request.getSession(true).getId());
			synchronized(userConnectionManager) {
				userConnectionManager.removeUserConnection(channelUserManager.getUser(userEmailAddress));
				//channelUserManager.getUser(userEmailAddress).setConnection(null);
			}
			event.close();
		}
	}

	private void processClientActionRequest(CometEvent event) throws IOException {
		HttpServletRequest request = event.getHttpServletRequest();
		String action = request.getParameter("action");
		String userEmailAddress = (String)request.getSession(true).getAttribute("user");
		
		if (ChatClientAction.SEND.equals(action)) {
			log.info("user=" + userEmailAddress + " - Chat request for session: " + request.getSession(true).getId());
			String chatMessageStr = request.getParameter("chatMessage");
			if (chatMessageStr != null && !"".equals(chatMessageStr)) {
				User user = channelUserManager.getUser(userEmailAddress);
				messageSender.consume(chatMessageStr, user);
			} else {
				log.info("chatMessageStr=<blank>");
			}
		}
		else if (ChatClientAction.POLL.equals(action)) {
			log.info("user=" + userEmailAddress + " - Poll request for session: " + request.getSession(true).getId());
			String lastMessageIdStr = request.getParameter("lastMessageId");
			if (lastMessageIdStr != null && !"".equals(lastMessageIdStr)) {
				int lastMessageId = Integer.parseInt(lastMessageIdStr);
				User user = channelUserManager.getUser(userEmailAddress);
				user.setLastMessageId(lastMessageId);
				if (lastMessageId == -1) {
					messageSender.snapshot();
				}
			}
		}
	}

}
