package chat.dao;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import chat.model.Channel;
import chat.model.ChatMessage;
import chat.model.User;

public class ChannelDAOJdbcTemplateImpl implements ChannelDAO {
	
	private DataSource dataSource;
	
	public ChannelDAOJdbcTemplateImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void addMessage(String channelId, String message, Date date,
			User user) {
		// TODO Auto-generated method stub

	}

	public Map<Integer, ChatMessage> getMessages(String channelId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Integer, ChatMessage> getMessagesSince(String channelId,
			int lastMessageId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addUser(String channelId, User user) {
		// TODO Auto-generated method stub

	}

	public boolean removeUser(String channelId, User user) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<User> getUsers(String channelId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addChannel(Channel channel) {
		// TODO Auto-generated method stub

	}

	public boolean removeChannel(String channelId) {
		// TODO Auto-generated method stub
		return false;
	}

	public Channel getChannel(String channelId) {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(String channelId, String userEmailAddress) {
		// TODO Auto-generated method stub
		return null;
	}

}
