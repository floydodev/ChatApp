package chat.model;

import java.util.List;
import java.util.Map;


public interface ChannelDAO {

	public static String defaultName = "default";
	
	public String getName();
	
	public Map<Integer, ChatMessage> getMessagesMap();

	public void addMessage(ChatMessage message);

	public Map<Integer, ChatMessage> getMessages(int lastMessageId);

	public void addUser(User user);

	public boolean removeUser(User user);
	
	public List<User> getUsers();

}