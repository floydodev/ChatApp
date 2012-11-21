package chat.lod.singlechannel.servlet.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.lod.singlechannel.service.UserConnectionManager;
import chat.servlet.service.ConnectionLifecycleHandler;


public class ConnectionLifecycleHandlerImpl implements ConnectionLifecycleHandler {

	private static final Log log = LogFactory.getLog(ConnectionLifecycleHandlerImpl.class);
	private UserConnectionManager userConnectionManager;

	public ConnectionLifecycleHandlerImpl(UserConnectionManager userConnectionManager) {
		this.userConnectionManager = userConnectionManager;
	}
	
	public void handle(CometEvent event) throws UnsupportedOperationException, IOException, ServletException {
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();
		String userEmailAddress = (String)request.getSession(true).getAttribute("user");
		
		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			// Starts the long-polling cycle
			log.info("user=" + userEmailAddress + " - Begin for session: " + request.getSession(true).getId());
			event.setTimeout(900*1000*1000); /* timeout is 15 minutes */
			synchronized(userConnectionManager) {
				boolean added = userConnectionManager.addUserConnection(userEmailAddress, response.getWriter());
				if (!added) {
					log.error("Couldn't add connection for user:" + userEmailAddress);
					// should we throw an exception at this point?
				}
			}
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			log.info("user=" + userEmailAddress + " - Error for session: " + request.getSession(true).getId());
			synchronized(userConnectionManager) {
				userConnectionManager.removeUserConnection(userEmailAddress);
			}
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.END) {
			// Completes a long-polling cycle. The client is designed to start another cycle
			log.info("user=" + userEmailAddress + " - End for session: " + request.getSession(true).getId());
			synchronized(userConnectionManager) {
				userConnectionManager.removeUserConnection(userEmailAddress);
			}
			event.close();
		}
		
	}

}
