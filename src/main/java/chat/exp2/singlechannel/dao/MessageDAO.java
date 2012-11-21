package chat.exp2.singlechannel.dao;

import java.util.Date;
import java.util.Map;

import chat.exp2.singlechannel.dto.Message;
import chat.exp2.singlechannel.dto.User;

public interface MessageDAO {
	
	public boolean addMessage(String message, Date date, User user);
	
	public boolean hasMessages();

	public Map<Integer, Message> getMessagesSince(int messageId);


}
