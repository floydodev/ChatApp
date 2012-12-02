package chat.singlechannel.servlet;

//import static chat.beanFactory.BeanFactory._ChannelUserManager;
//import static chat.beanFactory.BeanFactory._MessengerService;
//import static chat.beanFactory.BeanFactory.getInstance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import chat.multichannel.servlet.service.ClientActionRequestHandler;
import chat.multichannel.servlet.service.ConnectionLifecycleHandler;
import chat.singlechannel.service.messaging.SnapshotMessengerService;

public class ChatMessageServlet extends HttpServlet implements CometProcessor {

	static private final Log log = LogFactory.getLog(ChatMessageServlet.class);
	
	private SnapshotMessengerService messengerService = null;
	private Thread messengerServiceThread = null;

	private ConnectionLifecycleHandler connectionLifecycleHandler;
	private ClientActionRequestHandler clientActionRequestHandler;
	
	public void init() throws ServletException {

		messengerService = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("messengerService", SnapshotMessengerService.class);
		
		connectionLifecycleHandler = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("connectionLifecycleHandler", ConnectionLifecycleHandler.class);
		clientActionRequestHandler = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("clientActionRequestHandler", ClientActionRequestHandler.class);


		messengerServiceThread = new Thread(messengerService, "SnapshotMessengerService[" + getServletContext().getContextPath() + "]");
		messengerServiceThread.setDaemon(true);
		messengerServiceThread.start();
	}

	@Override
	public void destroy() {
		messengerService.stop();
		try {
			long waitLengthSecs = 10;
			log.info("Wait for thread to stop (" + waitLengthSecs + " secs)");
			messengerServiceThread.join(waitLengthSecs * 1000);
			log.info("Thread stopped");
		} catch (InterruptedException e) {
			log.error("Caught InterruptedException - why didn't it stop?" + e);
			//e.printStackTrace();
		}
		messengerService = null;
	}
	
	public void event(CometEvent event) throws IOException, ServletException {
		switch (event.getEventType()) {
			case BEGIN :
			case END :
			case ERROR : {
				connectionLifecycleHandler.handle(event);
				break;
			}
			case READ : {
				clientActionRequestHandler.handle(event.getHttpServletRequest(), 
						event.getHttpServletRequest().getParameter("action"));
				break;
			}
			default: {
				log.info("Got some other event type: " + event.getEventType());
			}
		}

	}


}
