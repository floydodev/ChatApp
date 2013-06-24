package chat.singlechannel.dao;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.singlechannel.dto.Message;

public class MessagesDAOInMemoryImpl implements MessagesDAO {

	private final static Log log = LogFactory.getLog(MessagesDAOInMemoryImpl.class);
	private NavigableMap<Integer, Message> messageCache = new TreeMap<Integer, Message>();
	
	public MessagesDAOInMemoryImpl() {
		log.info("InMemoryImpl constructor called");		
	}
	
	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.MessagesDAO#put(int, chat.singlechannel.dto.Message)
	 */
	public void put(int id, Message message) {
		if (null == message) {
			throw new IllegalArgumentException("Message must not be null");
		}
		messageCache.put(id, message);
		messageCache.put(null, null);
		
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

}
