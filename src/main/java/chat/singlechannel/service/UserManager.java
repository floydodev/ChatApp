package chat.singlechannel.service;

import chat.singlechannel.dao.ChatRoom;

public class UserManager {
	
	private ChatRoom chatRoomDAO;
	
	public UserManager(ChatRoom chatRoomDAO) {
		this.chatRoomDAO = chatRoomDAO;
	}
	
	public void addUser(String emailAddress, String displayName) {
		chatRoomDAO.addUser(emailAddress, displayName);
	}
	
//	public boolean removeUser(String userEmailAddress) {
//		return chatRoomDAO.removeUser(userEmailAddress);
//	}

//	public void setUserLastMessageId(String userEmailAddress, int lastMessageId) {
//		chatRoomDAO.setUserLastMessageId(userEmailAddress, lastMessageId);
//	}
//
//	public boolean isUser(String userEmailAddress) {
//		return channelDAO.hasUser(userEmailAddress);
//	

//	public boolean hasUser(String userEmailAddress) {
//		// TODO Auto-generated method stub
//		return chatRoomDAO.hasUser(userEmailAddress);
//	}

//	public int getUserLastMessageId(String userEmailAddress) {
//		return chatRoomDAO.getUserLastMessageId(userEmailAddress);
//	}
	
	
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
