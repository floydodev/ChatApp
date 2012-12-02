package chat.singlechannel.dao;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

import chat.singlechannel.dto.Message;

public class MessagesDAOInMemoryImpl implements MessagesDAO {

	private NavigableMap<Integer, Message> messageCache = new TreeMap<Integer, Message>();
	
	public MessagesDAOInMemoryImpl() {
		
	}
	
	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.MessagesDAO#put(int, chat.singlechannel.dto.Message)
	 */
	public void put(int id, Message message) {
		if (null == message) {
			throw new IllegalArgumentException("Message must not be null");
		}
		messageCache.put(id, message);
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.MessagesDAO#isEmpty()
	 */
	public boolean isEmpty() {
		return messageCache.isEmpty();
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.MessagesDAO#lastKey()
	 */
	public int lastKey() {
		if (messageCache.isEmpty()) {
			return 0;
		} else {
			return messageCache.lastKey();
		}
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.MessagesDAO#tailMap(int, boolean)
	 */
	public NavigableMap<Integer, Message> tailMap(int userLastMessageId) {
		if (messageCache.isEmpty()) {
			return new TreeMap<Integer, Message>();
		} else {
			return messageCache.tailMap(userLastMessageId, false);
		}
	}

}
