package chat.exp2.singlechannel.service;

public interface UserManager {
	
	public void addUser(String emailAddress, String displayName);
	public boolean hasUser(String userEmailAddress);
	public boolean removeUser(String emailAddress);
	public void setUserLastMessageId(String userEmailAddress, int lastMessageId);
	public int getUserLastMessageId(String userEmailAddress);

}
