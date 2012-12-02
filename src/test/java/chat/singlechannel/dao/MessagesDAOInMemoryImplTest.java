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

public class MessagesDAOInMemoryImplTest {

	private MessagesDAOTest messagesDAOTest;
	
	@Before
	public void setUp() throws Exception {
		MessagesDAO messagesDAO = new MessagesDAOInMemoryImpl();
		messagesDAOTest = new MessagesDAOTest(messagesDAO);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public void put_Fails_given_NullArgs() {
		messagesDAOTest.put_Fails_given_NullArgs();
	}
	
	@Test
	public void put_Succeeds_given_ValidArgs() {
		messagesDAOTest.put_Succeeds_given_ValidArgs();
	}
	
	@Test
	public void isEmpty_True_given_NewMessagesDAO() {
		messagesDAOTest.isEmpty_True_given_NewMessagesDAO();
	}
	
	@Test
	public void lastKey_Gives_KeyOfNthMessage() {
		messagesDAOTest.lastKey_Gives_KeyOfNthMessage();
	}
	
	@Test
	public void lastKey_ReturnsZero_GivenNoMessages() {
		messagesDAOTest.lastKey_ReturnsZero_GivenNoMessages();
	}

	@Test
	public void tailMap_ReturnsEmptyMap_GivenNoMessage() {
		messagesDAOTest.tailMap_ReturnsEmptyMap_GivenNoMessage();
	}

	@Test
	public void tailMap_ReturnsAllMessagesSince_GivenId() {
		messagesDAOTest.tailMap_ReturnsAllMessagesSince_GivenId();
	}
	
//
//	
//	@Test
//	public void put_Succeeds_given_ValidArgs() {
//		MessagesDAO messagesDAO = new MessagesDAOInMemoryImpl();
//		Message message = mock(Message.class);
//		assertThat(messagesDAO.isEmpty(), equalTo(true));
//		messagesDAO.put(message.getMessageId(), message);
//		assertThat(messagesDAO.isEmpty(), equalTo(false));
//	}
//	
//	
//	@Test
//	public void isEmpty_True_given_NewMessagesDAO() {
//		MessagesDAO messagesDAO = new MessagesDAOInMemoryImpl();
//		assertThat(messagesDAO.isEmpty(), equalTo(true));
//	}
//
//	@Test
//	public void lastKey_Gives_KeyOfNthMessage() {
//		MessagesDAO messagesDAO = new MessagesDAOInMemoryImpl();
//		Message message = mock(Message.class);
//		int n = 10;
//		for (int i = 1; i <= n; i++) {
//			messagesDAO.put(i, message);
//		}
//		assertThat(messagesDAO.lastKey(), equalTo(n));
//	}
//	
//	@Test
//	public void lastKey_ReturnsZero_GivenNoMessages() {
//		MessagesDAO messagesDAO = new MessagesDAOInMemoryImpl();
//		assertThat(messagesDAO.lastKey(), equalTo(0));
//	}
//
//	@Test
//	public void tailMap_ReturnsEmptyMap_GivenNoMessage() {
//		MessagesDAO messagesDAO = new MessagesDAOInMemoryImpl();
//		NavigableMap<Integer, Message> emptyMap = new TreeMap<Integer, Message>();
//		assertThat(messagesDAO.tailMap(5), equalTo(emptyMap));
//	}
//	
//	@Test
//	public void tailMap_ReturnsAllMessagesSince_GivenId() {
//		MessagesDAO messagesDAO = new MessagesDAOInMemoryImpl();
//		Message message = mock(Message.class);
//		int numMessages = 15;
//		int messagesSinceId = 5;
//		for (int i = 1; i <= numMessages; i++) {
//			messagesDAO.put(i, message);
//		}
//		
//		NavigableMap<Integer, Message> returnedMap = messagesDAO.tailMap(messagesSinceId);
//		assertThat(returnedMap.size(), equalTo(numMessages - messagesSinceId));
//
//		for (int i = 1; i <= (numMessages - messagesSinceId); i++) {
//			assertThat(returnedMap, hasKey(messagesSinceId + i));
//		}
//		
//	}

}
