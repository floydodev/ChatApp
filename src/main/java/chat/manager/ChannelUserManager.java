package chat.manager;

import java.util.HashMap;
import java.util.Map;

import chat.model.ChannelDAO;
import chat.model.User;

public class ChannelUserManager {
	
	private Map<String, ChannelDAO> channelMap = new HashMap<String, ChannelDAO>();
	
	public ChannelUserManager(ChannelDAO channel) {
		channelMap.put(channel.getName(), channel);
	}
	
	public void addUser(User user) {
		addUser(ChannelDAO.defaultName, user);
	}
	
	public void addUser(String channelId, User user) {
		channelMap.get(channelId).addUser(user);
	}
	
	public boolean removeUser(User user) {
		return removeUser(ChannelDAO.defaultName, user);
	}
	
	public boolean removeUser(String channelId, User user) {
		return channelMap.get(channelId).removeUser(user);
	}
}
