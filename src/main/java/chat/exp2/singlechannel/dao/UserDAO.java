package chat.exp2.singlechannel.dao;

import chat.exp2.singlechannel.dto.User;

public interface UserDAO {
	
	public User getUser(String userEmailAddress);
	public void addUser(String emailAddress, String displayName);
	public boolean removeUser(String userEmailAddress);
	public void setUserLastMessageId(String userEmailAddress, int lastMessageId);
	public int getUserLastMessageId(String userEmailAddress);
	public boolean hasUser(String userEmailAddress);

}
