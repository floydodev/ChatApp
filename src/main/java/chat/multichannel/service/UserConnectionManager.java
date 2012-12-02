package chat.multichannel.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import chat.multichannel.dao.ChannelDAO;
import chat.multichannel.model.Channel;
import chat.multichannel.model.User;

public class UserConnectionManager {

	private Map<User, HttpServletResponse> userConnectionMap;
	
	public UserConnectionManager() {
		this.userConnectionMap = new HashMap<User, HttpServletResponse>();
	}
	
	public Map<User, HttpServletResponse> getUserConnectionMap() {
		return userConnectionMap;
	}
	
	public HttpServletResponse addUserConnection(User user, HttpServletResponse connection) {
		return userConnectionMap.put(user, connection);
	}

	public HttpServletResponse removeUserConnection(User user) {
		return userConnectionMap.remove(user);
	}
	
	public HttpServletResponse getConnection(User user) {
		return userConnectionMap.get(user);
	}
	
}
