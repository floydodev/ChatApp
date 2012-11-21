package chat.lod.singlechannel.dao;

import java.util.Date;

public interface ChatRoomDAO {

	//public void addMessage(int id, String message, Date date, User user);

	public boolean addMessage(String message, Date date, String userEmailAddress);
	public boolean hasMessages();	
	public boolean hasNewMessages();
	public void newMessagesProcessed();
	
	public void addUser(String emailAddress, String displayName);
	public boolean hasUser(String userEmailAddress);
	public boolean removeUser(String emailAddress);
	public void setUserLastMessageId(String userEmailAddress, int lastMessageId);
	public Object getPendingMessages(String userEmailAddress);
	
}