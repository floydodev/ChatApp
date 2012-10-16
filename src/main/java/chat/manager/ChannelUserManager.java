package chat.manager;

import java.util.HashMap;
import java.util.Map;

import chat.dao.ChannelDAO;
import chat.model.Channel;
import chat.model.User;

public class ChannelUserManager {
	
	private ChannelDAO channelDAO;
	
	public ChannelUserManager(ChannelDAO channelDAO) {
		this.channelDAO = channelDAO;
	}
	
	public void addUser(User user) {
		addUser(Channel.DEFAULT_NAME, user);
	}
	
	public void addUser(String channelId, User user) {
		channelDAO.addUser(channelId, user);
	}
	
	public boolean removeUser(User user) {
		return removeUser(Channel.DEFAULT_NAME, user);
	}
	
	public boolean removeUser(String channelId, User user) {
		return channelDAO.removeUser(channelId, user);
	}
}
