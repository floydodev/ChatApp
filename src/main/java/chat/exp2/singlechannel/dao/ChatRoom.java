package chat.exp2.singlechannel.dao;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.exp2.singlechannel.dto.Message;
import chat.exp2.singlechannel.dto.User;
import chat.exp2.singlechannel.service.MessageManager;
import chat.exp2.singlechannel.service.MessagePusher;
import chat.exp2.singlechannel.service.UserManager;
import chat.util.MessageIdFountain;

import com.google.gson.Gson;


public class ChatRoom implements MessageManager, UserManager, MessagePusher {
	
	private boolean hasNewMessages = false;
	private NavigableMap<Integer, Message> messages;
	private Map<String, User> users;
	private final static Log log = LogFactory.getLog(ChatRoom.class);
	private MessageIdFountain messageIdFountain = null;
	private Map<String, Integer> lastMessageReceivedByUserMap = new HashMap<String, Integer>();
	
	public boolean addMessage(String message, Date date, String userEmailAddress) {
		if (userEmailAddress == null || "".equals(userEmailAddress)) {
			log.error("Cannot add message: userEmailAddress is not present");
			return false;
		}
		if (message == null || "".equals(message)) {
			log.error("Cannot add message: messageText is not present");
			return false;
		}
		int id = messageIdFountain.getNextId();
		return messages.put(id, new Message(message, date, users.get(userEmailAddress), id)) != null;
	}

	public void addUser(String emailAddress, String displayName) {
		users.put(emailAddress, new User(displayName, emailAddress));
	}

	public boolean removeUser(String emailAddress) {
		return users.remove(emailAddress) != null;
	}
	
	public boolean hasMessages() {
		return !messages.isEmpty();
	}

	public void setUserLastMessageId(String userEmailAddress, int lastMessageId) {
		users.get(userEmailAddress).setLastMessageId(lastMessageId);
	}

	public boolean hasUser(String userEmailAddress) {
		return users.containsKey(userEmailAddress);
	}

	private Map<Integer, Message> getPendingMessages(String userEmailAddress) {
		int lastMessageId = users.get(userEmailAddress).getLastMessageId();
		if (lastMessageId == -1) {
			// send up to 'snapshotSize' num of messages back to the client
			return getLastXMessages(10);
		} else {
			// send only the messages not yet sent during this connection session
			return getMessagesSince(lastMessageId);
		}
	}
	
	public void pushPendingMessages(String userEmailAddress, PrintWriter writer) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(getPendingMessages(userEmailAddress));
		writer.println(jsonString);
		writer.flush();
		writer.close();
	}
	
	public boolean hasNewMessages() {
		return hasNewMessages;
	}
	
	public void newMessagesProcessed() {
		this.hasNewMessages = false;
	}
	
	private Map<Integer, Message> getLastXMessages(int x) {
		Map<Integer, Message> messageMap = new HashMap<Integer, Message>();
		int lastMessageId = getLastMessageId();
		// after this point getLastMessageId may still change before we do sinceMessageId 
		// calculation. So a local variable is used to avoid threading issues.
		// Any "missed" message will be picked up when client polls next time around
		// i.e. any incoming message that bumped "lastMessageId" whilst this code executed
		if (lastMessageId > 0) {
			int sinceMessageId = lastMessageId < x ? 0 : lastMessageId - x;
			messageMap = getMessagesSince(sinceMessageId);
		}
		return messageMap;
	}

	public Map<Integer, Message> getMessagesSince(int lastMessageId) {
		return messages.tailMap(lastMessageId, false);
	}
	
	private int getLastMessageId() {
		return messages.lastKey();
	}

	public void pushPendingMessages(String userEmailAddress,
			PrintWriter writer, Map<Integer, Message> messages) {
		// TODO Auto-generated method stub
		
	}

	public int getUserLastMessageId(String userEmailAddress) {
		// TODO Auto-generated method stub
		return 0;
	}

}
