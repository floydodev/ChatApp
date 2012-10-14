package chat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class InMemoryChannelImpl implements Channel {
	
	private NavigableMap<Integer, ChatMessage> messageMap = new TreeMap<Integer, ChatMessage>();
	private List<User> users = new ArrayList<User>();
	private String name;
	
	public InMemoryChannelImpl() {
		this.name= Channel.defaultName;
	}

	public InMemoryChannelImpl(String name) {
		this.name = name ;
	}
	public String getName() {
		return name;
	}
	
	public Map<Integer, ChatMessage> getMessagesMap() {
		return messageMap;
	}
	
	public void addMessage(ChatMessage message) {
		messageMap.put(message.getMessageId(), message);
	}

	public Map<Integer, ChatMessage> getMessages(int lastMessageId) {
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
}
