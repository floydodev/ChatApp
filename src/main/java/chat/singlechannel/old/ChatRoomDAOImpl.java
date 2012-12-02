package chat.singlechannel.old;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.singlechannel.dao.ChatRoom;
import chat.singlechannel.dto.Message;
import chat.singlechannel.dto.User;

public class ChatRoomDAOImpl implements ChatRoom {
	
	private NavigableMap<Integer, Message> messageRepo = new TreeMap<Integer, Message>();
	private Map<String, User> userRepo = new HashMap<String, User>();
	private final static Log log = LogFactory.getLog(ChatRoomDAOImpl.class);
	
	private int snapshotSize = 10;
	
	public ChatRoomDAOImpl() {
	}
	
	public ChatRoomDAOImpl(int snapshotSize) {
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
		
		if (userRepo.containsKey(userEmailAddress)) {
			messageRepo.put(id, new Message(id, message, date, userRepo.get(userEmailAddress)));
		} else {
			throw new IllegalArgumentException("Cannot add messages for non-existent user:" + userEmailAddress);
		}
	}

	public boolean hasMessages() {
		return !messageRepo.isEmpty();
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
		userRepo.put(emailAddress, new User(emailAddress, displayName));
	}


	public void setUserLastMessageId(String userEmailAddress, int lastMessageId) {
		if (userRepo.containsKey(userEmailAddress)) {
			userRepo.get(userEmailAddress).setLastMessageId(lastMessageId);
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
		if (!userRepo.containsKey(userEmailAddress)) {
			throw new IllegalArgumentException("Cannot get messages for non-existent user:" + userEmailAddress);
		}
		if (messageRepo.isEmpty()) {
			return new TreeMap<Integer, Message>();
		}
		int userLastMessageId = userRepo.get(userEmailAddress).getLastMessageId();
		if (userLastMessageId == 0) {
			int repoLastMessageId = messageRepo.lastKey();
			int sinceMessageId = repoLastMessageId < snapshotSize ? 0 : repoLastMessageId - snapshotSize;
			userLastMessageId = sinceMessageId;
		}
		return messageRepo.tailMap(userLastMessageId, false);
	}


}
