package chat.multichannel.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chat.multichannel.model.Channel;
import chat.multichannel.model.ChatMessage;
import chat.multichannel.model.User;


public interface ChannelDAO {

	public void addMessage(String channelId, String message, Date date,	User user);

	public Map<Integer, ChatMessage> getMessages(String channelId);
	
	public Map<Integer, ChatMessage> getMessagesSince(String channelId, int lastMessageId);
	
	public void addUser(String channelId, User user);

	public boolean removeUser(String channelId, User user);
	
	//public User getUser(String channelId, String emailId);
	
	public Set<User> getUsers(String channelId);

	public void addChannel(Channel channel);

	public boolean removeChannel(String channelId);

	public Channel getChannel(String channelId);

	public User getUser(String channelId, String userEmailAddress);

	public int getLastMessageId(String channelId);

}