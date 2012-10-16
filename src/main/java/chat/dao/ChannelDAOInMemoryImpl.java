package chat.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.model.Channel;
import chat.model.ChatMessage;
import chat.model.User;


public class ChannelDAOInMemoryImpl implements ChannelDAO {
	
	private Map<String, Channel> channelMap = new HashMap<String, Channel>();
	
	public ChannelDAOInMemoryImpl(Channel channel) {
		channelMap.put(channel.getName(), channel);
	}
	
	// Channel Message Management methods

	public void addMessage(String channelId, String message, Date date,
			User user) {
		channelMap.get(channelId).addMessage(message, date, user);
	}
	
	public Map<Integer, ChatMessage> getMessages(String channelId) {
		return channelMap.get(channelId).getMessages();
	}

	public Map<Integer, ChatMessage> getMessagesSince(String channelId, int lastMessageId) {
		return channelMap.get(channelId).getMessagesSince(lastMessageId);
	}

	// Channel User Management methods
	
	public void addUser(String channelId, User user) {
		channelMap.get(channelId).addUser(user);
	}

//	public User getUser(String channelId, String emailId) {
//		return channelMap.get(channelId).getUser(emailId);
//	}
	
	public List<User> getUsers(String channelId) {
		return channelMap.get(channelId).getUsers();
	}
	
	public boolean removeUser(String channelId, User user) {
		return channelMap.get(channelId).removeUser(user);
	}

	// Channel Admin Management methods
	
	public void addChannel(Channel channel) {
		channelMap.put(channel.getName(), channel);
	}

	public boolean removeChannel(String channelId) {
		return channelMap.remove(channelId) != null;
	}

	public Channel getChannel(String channelId) {
		return channelMap.get(channelId);
	}
}
