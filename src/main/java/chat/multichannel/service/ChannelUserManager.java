package chat.multichannel.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import chat.multichannel.dao.ChannelDAO;
import chat.multichannel.model.Channel;
import chat.multichannel.model.User;

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
	
	public User getUser(String userEmailAddress) {
		return getUser(Channel.DEFAULT_NAME, userEmailAddress);
	}
	
	public User getUser(String channelId, String userEmailAddress) {
		return channelDAO.getUser(channelId, userEmailAddress);
	}
	
	public boolean removeUser(User user) {
		return removeUser(Channel.DEFAULT_NAME, user);
	}
	
	public boolean removeUser(String channelId, User user) {
		return channelDAO.removeUser(channelId, user);
	}
	
	public Set<User> getUsers() {
		return getUsers(Channel.DEFAULT_NAME);
	}

	public Set<User> getUsers(String channelId) {
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
