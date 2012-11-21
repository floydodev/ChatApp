package chat.exp2.singlechannel.dao;

import java.util.HashMap;
import java.util.Map;

import chat.exp2.singlechannel.dto.User;

public class UserDAOInMemoryImpl implements UserDAO {

	private Map<String, User> users = new HashMap<String, User>();
	
	public User getUser(String userEmailAddress) {
		return users.get(userEmailAddress);
	}

	public void addUser(String emailAddress, String displayName) {
		users.put(emailAddress, new User(displayName, emailAddress));
	}

	public boolean removeUser(String userEmailAddress) {
		return users.remove(userEmailAddress) != null;
	}

	public void setUserLastMessageId(String userEmailAddress, int lastMessageId) {
		users.get(userEmailAddress).setLastMessageId(lastMessageId);
	}

	public int getUserLastMessageId(String userEmailAddress) {
		return users.get(userEmailAddress).getLastMessageId();
	}

	public boolean hasUser(String userEmailAddress) {
		return users.containsKey(userEmailAddress);
	}

}
