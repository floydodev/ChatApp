package chat.singlechannel.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import chat.singlechannel.dto.Message;
import chat.singlechannel.dto.User;

@Ignore
public class MessagesDAOJdbcImplTest {

	
	private final static Log log = LogFactory
			.getLog(MessagesDAOJdbcImplTest.class);
	private MessagesDAOCodeReuse messagesDAOTest;
	private MessagesDAOJdbcImpl messagesDAO;

	@Before
	public void setUp() throws Exception {
		messagesDAO = new MessagesDAOJdbcImpl();
		messagesDAO.setJdbcTemplate(mock(JdbcTemplate.class));
		messagesDAOTest = new MessagesDAOCodeReuse(messagesDAO);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void put_Fails_given_NullArgs() {
		messagesDAOTest.put_Fails_given_NullArgs();
	}

	@Test
	public void testName() throws Exception {
		
		// Given (Setup SUT)

		// When

		// Then (Verify SUT)

		// Teardown/Clean up

	}
	@Test
	public void put_Succeeds_given_ValidArgs() {
		Message message = mock(Message.class);
		User user = mock(User.class);
		when(message.getUser()).thenReturn(user);
		messagesDAO.put(1, message);
	}

	@Test
	public void isEmpty_True_given_NewMessagesDAO() {
		messagesDAOTest.isEmpty_True_given_NewMessagesDAO();
	}

	@Test
	public void lastKey_Gives_KeyOfNthMessage() {
		Message message = mock(Message.class);
		User user = mock(User.class);
		when(message.getUser()).thenReturn(user);
		int n = 10;
		for (int i = 1; i <= n; i++) {
			messagesDAO.put(i, message);
		}
		assertThat(messagesDAO.lastKey(), equalTo(n));
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
		Message message = mock(Message.class);
		User user = mock(User.class);
		when(message.getUser()).thenReturn(user);
		int numMessages = 15;
		int messagesSinceId = 5;
		for (int i = 1; i <= numMessages; i++) {
			messagesDAO.put(i, message);
		}

		NavigableMap<Integer, Message> returnedMap = messagesDAO
				.tailMap(messagesSinceId);
		assertThat(returnedMap.size(), equalTo(numMessages - messagesSinceId));

		for (int i = 1; i <= (numMessages - messagesSinceId); i++) {
			assertThat(returnedMap, hasKey(messagesSinceId + i));
		}
	}

	// @Test
	// public void initialisedDAO_Contains_PreviousSessionMessages() {
	// fail();
	// }

}
