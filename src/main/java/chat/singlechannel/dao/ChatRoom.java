package chat.singlechannel.dao;

import java.util.Date;
import java.util.NavigableMap;

import chat.singlechannel.dto.Message;

public interface ChatRoom {

	public void addMessage(int id, String message, Date date, String userEmailAddress);
	public boolean hasMessages();	
	public void addUser(String emailAddress, String displayName);
	public void setUserLastMessageId(String userEmailAddress, int lastMessageId);
	public NavigableMap<Integer, Message> getPendingMessages(String userEmailAddress);
	
}