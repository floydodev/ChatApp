package chat.multichannel.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.comet.CometEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import chat.multichannel.servlet.service.ClientActionRequestHandler;
import chat.multichannel.servlet.service.ConnectionLifecycleHandler;

public class ChatMessageServletRefactored extends ChatMessageServlet {
	
	private static final Log log = LogFactory.getLog(ChatMessageServletRefactored.class);
	private ConnectionLifecycleHandler connectionLifecycleHandler;
	private ClientActionRequestHandler clientActionRequestHandler;
	
	public void init() throws ServletException {
		super.init();
		connectionLifecycleHandler = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("connectionLifecycleHandler", ConnectionLifecycleHandler.class);
		clientActionRequestHandler = WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean("clientActionRequestHandler", ClientActionRequestHandler.class);
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
