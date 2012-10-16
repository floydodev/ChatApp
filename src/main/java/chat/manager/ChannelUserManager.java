package chat.manager;

import java.util.List;

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
	
	public List<User> getUsers() {
		return getUsers(Channel.DEFAULT_NAME);
	}

	public List<User> getUsers(String channelId) {
		return channelDAO.getUsers(channelId);
	}

//	public User getUser(String email) {
//		return getUser(Channel.DEFAULT_NAME, email);
//	}
//	
//	public User getUser(String channelId, String email) {
//		return channelDAO.getUser(channelId, email);
//	}
}
