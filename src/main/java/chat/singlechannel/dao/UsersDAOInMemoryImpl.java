package chat.singlechannel.dao;

import java.util.HashMap;
import java.util.Map;

import chat.singlechannel.dto.User;

public class UsersDAOInMemoryImpl implements UsersDAO {

	private Map<String, User> userRepo = new HashMap<String, User>();
	
	public UsersDAOInMemoryImpl() {
	}
	
	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.UsersDAO#containsKey(java.lang.String)
	 */
	public boolean containsKey(String userEmailAddress) {
		return userRepo.containsKey(userEmailAddress);
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.UsersDAO#get(java.lang.String)
	 */
	public User get(String userEmailAddress) {
		// TODO Auto-generated method stub
		return userRepo.get(userEmailAddress);
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.UsersDAO#put(java.lang.String, chat.singlechannel.dto.User)
	 */
	public void put(String emailAddress, User user) {
		userRepo.put(emailAddress, user);
	}

	public int getLastMessageId(String userEmailAddress) {
		return userRepo.get(userEmailAddress).getLastMessageId();
	}

	public void setLastMessageId(String userEmailAddress, int lastMessageId) {
		userRepo.get(userEmailAddress).setLastMessageId(lastMessageId);
	}

}
