package chat.exp2.singlechannel.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import chat.exp2.singlechannel.dto.Message;
import chat.exp2.singlechannel.dto.User;
import chat.util.MessageIdFountain;

public class MessageDAOInMemoryImpl implements MessageDAO {

	private NavigableMap<Integer, Message> messages = new TreeMap<Integer, Message>();
	private MessageIdFountain idFountain;	

	public boolean hasMessages() {
		return !messages.isEmpty();
	}

	private Map<Integer, Message> getPendingMessages(int lastMessageId) {
		if (lastMessageId == -1) {
			// send up to 'snapshotSize' num of messages back to the client
			return getLastXMessages(10);
		} else {
			// send only the messages not yet sent during this connection session
			return getMessagesSince(lastMessageId);
		}
	}
	
	private Map<Integer, Message> getLastXMessages(int x) {
		Map<Integer, Message> messageMap = new HashMap<Integer, Message>();
		int lastMessageId = getLastMessageId();
		// after this point getLastMessageId may still change before we do sinceMessageId 
		// calculation. So a local variable is used to avoid threading issues.
		// Any "missed" message will be picked up when client polls next time around
		// i.e. any incoming message that bumped "lastMessageId" whilst this code executed
		if (lastMessageId > 0) {
			int sinceMessageId = lastMessageId < x ? 0 : lastMessageId - x;
			messageMap = getMessagesSince(sinceMessageId);
		}
		return messageMap;
	}
	
	public Map<Integer, Message> getMessagesSince(int lastMessageId) {
		return messages.tailMap(lastMessageId, false);
	}
	
	private int getLastMessageId() {
		return messages.lastKey();
	}

	public boolean addMessage(String message, Date date, User user) {
		// TODO Auto-generated method stub
		int id = idFountain.getNextId();
		return messages.put(id, new Message(message, date, user, id)) != null;
	}

}
