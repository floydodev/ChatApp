package chat.multichannel.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.multichannel.model.ChatMessage;
import chat.multichannel.model.User;

import com.google.gson.Gson;


public class BasicMessengerService implements MessengerService {

	static final Log log = LogFactory.getLog(BasicMessengerService.class);
	private Map<Integer, ChatMessage> messages = new HashMap<Integer, ChatMessage>();
	private UserConnectionManager userConnectionManager = null;
	private boolean running = true;

	public BasicMessengerService(UserConnectionManager userConnectionManager) {
		this.userConnectionManager = userConnectionManager;
	}

	public void stop() {
		running  = false;
	}

	public void run() {
		while (running) {
			publish();
		}
	}
	
	/**
	 * Add message for sending.
	 */
	public void consume(String messageText, User user) {
		synchronized (messages) {
			ChatMessage message = new ChatMessage(messageText, Calendar.getInstance().getTime(), user, ++messageId);
			messages.put(message.getMessageId(), message);
			messages.notify();
		}
	}
	
	private static int messageId = 0;
	
	public void snapshot() {
		// do nothing.
	}
	
	public void publish() {
		if (messages.size() == 0) {
			try {
				synchronized (messages) {
					messages.wait();
				}
			} catch (InterruptedException e) {
				// Ignore
			}
		}
	
		synchronized (userConnectionManager) {
			//String[] pendingMessages = null;
			// ChatMessage[] pendingMessages = null;
			Map<Integer, ChatMessage> pendingMessages = new HashMap<Integer, ChatMessage>();
			synchronized (messages) {
				pendingMessages.putAll(messages);
				messages.clear();
			}
			// Send any pending messages to any logged in users
			for (User user : userConnectionManager.getUserConnectionMap().keySet()) {
				try {
					PrintWriter writer = userConnectionManager.getUserConnectionMap().get(user).getWriter();
					Gson gson = new Gson();
					String jsonString = gson.toJson(pendingMessages); 
					log.info("Sending json message: " + jsonString + " to user:" + user.getEmailAddress());
					writer.println(jsonString);
					writer.flush();
					writer.close();	/* the response will not be sent until the writer is closed */
				} catch (IOException e) {
					log.error("IOExeption sending message", e);
				}
			}
	
		}
	}


}