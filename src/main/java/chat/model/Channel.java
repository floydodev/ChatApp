package chat.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;


public class Channel {
	
	private NavigableMap<Integer, ChatMessage> messageMap = new TreeMap<Integer, ChatMessage>();
	private List<User> users = new ArrayList<User>();
	private String name;
	
	MessageIdFountain messageIdFountain = null;

	public static final String DEFAULT_NAME = "default";
	
	public Channel() {
		this(DEFAULT_NAME, new MessageIdFountainAtomicIntImpl());
	}

	public Channel(String name) {
		this(name, new MessageIdFountainAtomicIntImpl());
	}

	public Channel(String name, MessageIdFountain idFountain) {
		this.name = DEFAULT_NAME;
		this.messageIdFountain = idFountain;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<Integer, ChatMessage> getMessages() {
		return messageMap;
	}

	public void addMessage(ChatMessage message) {
		messageMap.put(message.getMessageId(), message);
	}

	public Map<Integer, ChatMessage> getMessagesSince(int lastMessageId) {
		return messageMap.tailMap(lastMessageId + 1);
	}

	public void addUser(User user) {
		users.add(user);
	}

	public boolean removeUser(User user) {
		return users.remove(user);
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void addMessage(String message, Date date, User user) {
		ChatMessage chatMessage = new ChatMessage(message, date, user, messageIdFountain.getNextId());
		messageMap.put(chatMessage.getMessageId(), chatMessage);
	}
	
	public int getLastMessageId() {
		return messageIdFountain.getLastId();
	}

	public User getUser(String emailId) {
		// TODO Auto-generated method stub
		return null;
	}

}