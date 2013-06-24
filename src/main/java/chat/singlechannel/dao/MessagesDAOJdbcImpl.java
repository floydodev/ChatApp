package chat.singlechannel.dao;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import chat.singlechannel.dto.Message;
import chat.singlechannel.dto.User;

public class MessagesDAOJdbcImpl extends JdbcDaoSupport implements MessagesDAO {

	private final static Log log = LogFactory.getLog(UsersDAOJdbcImpl.class);
	private NavigableMap<Integer, Message> cache = new TreeMap<Integer, Message>();
	
	public MessagesDAOJdbcImpl() {
		log.info("JdbcImpl constructor called");
	}
	
	public void put(int id, Message message) {
		if (message == null) {
			throw new IllegalArgumentException("Message is null. Cannot persist!");
		}
		User user = message.getUser();
		String emailAddress = user.getEmailAddress();
		String sql = "insert into Messages (id, text, timestamp, user) values (?, ?, ?, ?)";
		getJdbcTemplate().update(sql, id, message.getText(), message.getTimestamp(), emailAddress);
		cache.put(id, message);
		log.info("Inserted 1 message into the DB + cache. Message Id: " + id);
	}

	public boolean isEmpty() {
		return cache.isEmpty() ? true: false;
	}

	public int lastKey() {
		if (isEmpty()) {
			return 0;
		} else {
			return cache.lastKey();
		}
	}

	public NavigableMap<Integer, Message> tailMap(int userLastMessageId) {
		return cache.tailMap(userLastMessageId, false);
	}

}
