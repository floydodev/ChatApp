package chat.exp2.singlechannel.service;

import chat.exp2.singlechannel.dao.UserDAO;

public class UserManagerImpl implements UserManager {
	
	//private OldChatRoomIntf chatRoomDAO;
	private UserDAO userDAO;
	
	public UserManagerImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void addUser(String emailAddress, String displayName) {
		userDAO.addUser(emailAddress, displayName);
		//chatRoomDAO.addUser(emailAddress, displayName);
	}
	
	public boolean removeUser(String userEmailAddress) {
		return userDAO.removeUser(userEmailAddress);
	}

	public void setUserLastMessageId(String userEmailAddress, int lastMessageId) {
		userDAO.setUserLastMessageId(userEmailAddress, lastMessageId);
	}
//
//	public boolean isUser(String userEmailAddress) {
//		return channelDAO.hasUser(userEmailAddress);
//	

	public boolean hasUser(String userEmailAddress) {
		// TODO Auto-generated method stub
		return userDAO.hasUser(userEmailAddress);
	}

	public int getUserLastMessageId(String userEmailAddress) {
		return userDAO.getUserLastMessageId(userEmailAddress);
	}
	
	
//	public Set<User> getUsers() {
//		return channelDAO.getUsers();
//	}

//	public User getUser(String email) {
//		return getUser(Channel.DEFAULT_NAME, email);
//	}
//	
//	public User getUser(String channelId, String email) {
//		return channelDAO.getUser(channelId, email);
//	}
}
