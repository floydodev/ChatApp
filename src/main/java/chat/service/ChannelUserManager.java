package chat.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import chat.dao.ChannelDAO;
import chat.model.Channel;
import chat.model.User;

public class ChannelUserManager {
	
	private ChannelDAO channelDAO;
	private Map<User, HttpServletResponse> userConnectionMap;
	
	public ChannelUserManager(ChannelDAO channelDAO) {
		this.channelDAO = channelDAO;
		this.userConnectionMap = new HashMap<User, HttpServletResponse>();
	}
	
	public Map<User, HttpServletResponse> getUserConnectionMap() {
		return userConnectionMap;
	}
	public void addUser(User user) {
		addUser(Channel.DEFAULT_NAME, user);
	}
	
	public HttpServletResponse addUserConnection(User user, HttpServletResponse connection) {
		return userConnectionMap.put(user, connection);
	}

	public HttpServletResponse removeUserConnection(User user) {
		return userConnectionMap.remove(user);
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
