package chat.singlechannel.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.singlechannel.dto.Message;
import chat.singlechannel.dao.MessagesDAO;

public class MessagesDAOCodeReuse {
	
	private MessagesDAO messagesDAO;
	
	public MessagesDAOCodeReuse(MessagesDAO messagesDAO) {
		this.messagesDAO = messagesDAO;
	}

	@Before
	public void setUp() throws Exception {
		//
	}

	@After
	public void tearDown() throws Exception {
		//
	}

	//@Test(expected=IllegalArgumentException.class)
	public void put_Fails_given_NullArgs() {
		messagesDAO.put(1, null);
	}

	
	//@Test
	public void put_Succeeds_given_ValidArgs() {
		assertThat(messagesDAO.isEmpty(), equalTo(true));
		Message message = mock(Message.class);
		messagesDAO.put(message.getMessageId(), message);
		assertThat(messagesDAO.isEmpty(), equalTo(false));
	}
	
	
	//@Test
	public void isEmpty_True_given_NewMessagesDAO() {
		assertThat(messagesDAO.isEmpty(), equalTo(true));
	}

	//@Test
	public void lastKey_Gives_KeyOfNthMessage() {
		Message message = mock(Message.class);
		int n = 10;
		for (int i = 1; i <= n; i++) {
			messagesDAO.put(i, message);
		}
		assertThat(messagesDAO.lastKey(), equalTo(n));
	}

	//@Test
	public void lastKey_ReturnsZero_GivenNoMessages() {
		assertThat(messagesDAO.lastKey(), equalTo(0));
	}

	//@Test
	public void tailMap_ReturnsEmptyMap_GivenNoMessage() {
		NavigableMap<Integer, Message> emptyMap = new TreeMap<Integer, Message>();
		assertThat(messagesDAO.tailMap(5), equalTo(emptyMap));
	}
	
	//@Test
	public void tailMap_ReturnsAllMessagesSince_GivenId() {
		Message message = mock(Message.class);
		int numMessages = 15;
		int messagesSinceId = 5;
		for (int i = 1; i <= numMessages; i++) {
			messagesDAO.put(i, message);
		}
		
		NavigableMap<Integer, Message> returnedMap = messagesDAO.tailMap(messagesSinceId);
		assertThat(returnedMap.size(), equalTo(numMessages - messagesSinceId));

		for (int i = 1; i <= (numMessages - messagesSinceId); i++) {
			assertThat(returnedMap, hasKey(messagesSinceId + i));
		}
		
	}

}
