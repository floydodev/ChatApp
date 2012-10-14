package chat.manager;

import java.util.HashMap;
import java.util.Map;

import chat.model.Channel;
import chat.model.User;

public class ChannelUserManager {
	
	private Map<String, Channel> channelMap = new HashMap<String, Channel>();
	
	public ChannelUserManager(Channel channel) {
		channelMap.put(channel.getName(), channel);
	}
	
	public void addUser(User user) {
		addUser(Channel.defaultName, user);
	}
	
	public void addUser(String channelId, User user) {
		channelMap.get(channelId).addUser(user);
	}
	
	public boolean removeUser(User user) {
		return removeUser(Channel.defaultName, user);
	}
	
	public boolean removeUser(String channelId, User user) {
		return channelMap.get(channelId).removeUser(user);
	}
}
