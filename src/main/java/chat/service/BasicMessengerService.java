package chat.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.model.ChatMessage;
import chat.model.User;

import com.google.gson.Gson;


public class BasicMessengerService extends MessengerService {

	static final Log log = LogFactory.getLog(BasicMessengerService.class);
	private Map<Integer, ChatMessage> messages = new HashMap<Integer, ChatMessage>();
	private ChannelUserManager channelUserManager = null;

	public BasicMessengerService(ChannelUserManager channelUserManager) {
		this.channelUserManager  = channelUserManager;
	}

	/**
	 * Add message for sending.
	 */
	public void receive(ChatMessage message) {
		synchronized (messages) {
			messages.put(message.getMessageId(), message);
			messages.notify();
		}
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
	
		synchronized (channelUserManager) {
			//String[] pendingMessages = null;
			// ChatMessage[] pendingMessages = null;
			Map<Integer, ChatMessage> pendingMessages = new HashMap<Integer, ChatMessage>();
			synchronized (messages) {
				pendingMessages.putAll(messages);
				messages.clear();
			}
			// Send any pending messages to any logged in users
			for (User user : channelUserManager.getUserConnectionMap().keySet()) {
				try {
					PrintWriter writer = channelUserManager.getUserConnectionMap().get(user).getWriter();
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