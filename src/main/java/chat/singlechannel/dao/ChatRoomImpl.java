package chat.singlechannel.dao;

import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import chat.singlechannel.dto.Message;
import chat.singlechannel.dto.User;

public class ChatRoomImpl implements ChatRoom {
	
	private MessagesDAO messagesDAO;
	private UsersDAO usersDAO;
	private final static Log log = LogFactory.getLog(ChatRoomImpl.class);
	private int snapshotSize = 10;
	
	public ChatRoomImpl(MessagesDAO messagesDAO, UsersDAO usersDAO) {
		this.messagesDAO = messagesDAO;
		this.usersDAO = usersDAO;
	}

	public ChatRoomImpl(int snapshotSize, MessagesDAO messagesDAO, UsersDAO usersDAO) {
		this(messagesDAO, usersDAO);
		this.snapshotSize = snapshotSize;
	}

	public void addMessage(int id, String message, Date date, String userEmailAddress) {
		if (message == null) {
			throw new IllegalArgumentException("Message cannot be null");
		}
		if("".equals(message.trim())) {
			throw new IllegalArgumentException("Message cannot be blank");
		}
		if (userEmailAddress == null) {
			throw new IllegalArgumentException("User email address cannot be null");
		}
		if ("".equals(userEmailAddress.trim())) {
			throw new IllegalArgumentException("User email address cannot be blank");
		}
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		
		if (usersDAO.containsKey(userEmailAddress)) {
			messagesDAO.put(id, new Message(id, message, date, usersDAO.get(userEmailAddress)));
		} else {
			throw new IllegalArgumentException("Cannot add messages for non-existent user:" + userEmailAddress);
		}
	}

	public boolean hasMessages() {
		return !messagesDAO.isEmpty();
	}

	public void addUser(String emailAddress, String displayName) {
		if (emailAddress == null) {
			throw new IllegalArgumentException("Email address cannot be null");
		}
		if (displayName == null) {
			throw new IllegalArgumentException("Display name cannot be null");
		}
		if ("".equals(emailAddress.trim())) {
			throw new IllegalArgumentException("Email address cannot be blank");
		}
		if ("".equals(displayName.trim())) {
			throw new IllegalArgumentException("Display name cannot be blank");
		}
		usersDAO.put(emailAddress, new User(emailAddress, displayName));
	}


	public void setUserLastMessageId(String userEmailAddress, int lastMessageId) {
		if (usersDAO.containsKey(userEmailAddress)) {
			usersDAO.setLastMessageId(userEmailAddress, lastMessageId);
		} else {
			throw new IllegalArgumentException("Cannot update last message id for non-existent user:" + userEmailAddress);
		}
	}
	
	public NavigableMap<Integer, Message> getPendingMessages(String userEmailAddress) {
		if (userEmailAddress == null) {
			throw new IllegalArgumentException("Email address cannot be null");
		}
		if ("".equals(userEmailAddress.trim())) {
			throw new IllegalArgumentException("Email address cannot be blank");
		}
		if (!usersDAO.containsKey(userEmailAddress)) {
			throw new IllegalArgumentException("Cannot get messages for non-existent user:" + userEmailAddress);
		}
		if (messagesDAO.isEmpty()) {
			return new TreeMap<Integer, Message>();
		}
		int userLastMessageId = usersDAO.getLastMessageId(userEmailAddress);
		if (userLastMessageId == 0) {
			int repoLastMessageId = messagesDAO.lastKey();
			int sinceMessageId = repoLastMessageId < snapshotSize ? 0 : repoLastMessageId - snapshotSize;
			userLastMessageId = sinceMessageId;
		}
		return messagesDAO.tailMap(userLastMessageId);
	}


}
