package chat.servlet.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.service.ChannelUserManager;
import chat.service.UserConnectionManager;

public class ConnectionLifecycleHandlerImpl implements ConnectionLifecycleHandler {

	private static final Log log = LogFactory.getLog(ConnectionLifecycleHandlerImpl.class);
	private UserConnectionManager userConnectionManager;
	private ChannelUserManager channelUserManager;
	
	public ConnectionLifecycleHandlerImpl(ChannelUserManager channelUserManager, UserConnectionManager userConnectionManager) {
		this.channelUserManager = channelUserManager;
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

}
