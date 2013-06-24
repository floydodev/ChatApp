package chat.singlechannel.dao;

//import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import chat.singlechannel.dao.ChatRoom;
import chat.singlechannel.dto.Message;
import chat.singlechannel.dto.User;
import chat.singlechannel.old.ChatRoomDAOImpl;

@Ignore
public class ChatRoomDAOImplUnits {
	
	//@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsForNonExistentUser() {
		String userEmailAddress = "test@test.com";
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		// Stub out a User object
//		User user = mock(User.class);
//		when(user.getEmailAddress()).thenReturn(userEmailAddress);
		//User user = new User(userEmailAddress, "Mr Test");
		// Setup the expected results
		//Message expectedMessage = new Message(messageId, message, timestamp, user);

		// Test the addMessage method
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
		// Should throw an exception
	}
	
	@Test
	public void testAddMessage_SucceedsForExistingUser() {
		String userEmailAddress = "test@test.com";
		String displayName = "Bungalow Bill";
		String message = "Hey";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		
		// Pass in real User object as it is used in equals method of Message
		// Can't use Mockito stub object for this reason.
		User user = new User(userEmailAddress, displayName);

		// Setup the expected results
		Message expectedMessage = new Message(messageId, message, timestamp, user);

		// Test the addMessage method
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser(user.getEmailAddress(), user.getDisplayName());
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
		// Get persisted messages
		Map<Integer, Message> pendingMessages = chatRoomDAO.getPendingMessages(userEmailAddress);

		assertThat(pendingMessages, hasValue(expectedMessage));
		assertThat(pendingMessages.entrySet(), hasSize(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsForBlank_EmailAddress() {
		String userEmailAddress = "";
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsForBlank_EmailAddress2() {
		String userEmailAddress = "          ";
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsFor_NullEmailAddress() {
		String userEmailAddress = null;
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsForNull_MessageText() {
		String userEmailAddress = "test@test.com";;
		String message = null;
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsForBlank_MessageText() {
		String userEmailAddress = "test@test.com";;
		String message = "";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsForBlank_MessageText2() {
		String userEmailAddress = "test@test.com";;
		String message = "     ";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddMessage_FailsForNull_Timestamp() {
		String userEmailAddress =  "test@test.com";;
		String message = "Test";
		int messageId = 1;
		Date timestamp = null;
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test
	public void testHasMessages_FailsWhenEmpty() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		assertFalse(chatRoomDAO.hasMessages());
	}
	
	
	
	@Test
	public void testHasMessages_SucceedsWhenPopulated() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		assertFalse(chatRoomDAO.hasMessages());
		
		String userEmailAddress = "test@test.com";
		String message = "Hey";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		User user = mock(User.class);
		when(user.getEmailAddress()).thenReturn(userEmailAddress);
		when(user.getDisplayName()).thenReturn("Bill");
		chatRoomDAO.addUser(user.getEmailAddress(), user.getDisplayName());
		
		chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
		assertTrue(chatRoomDAO.hasMessages());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddUser_FailsForBlank_UserEmailAddress() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser("", "Bill");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddUser_FailsForBlank_UserEmailAddress2() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser("   ", "Bill");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddUser_FailsForNull_UserEmailAddress() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser(null, "Bill");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddUser_FailsForBlank_DisplayName() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser("test@test.com", "");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddUser_FailsForBlank_DisplayName2() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser("test@test.com", "   ");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddUser_FailsForNull_DisplayName() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser("test@test.com", null);

	}
	
	@Test
	public void testAddUser_SucceedsForValidArgs() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser("test@test.com", "Bill");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetUserLastMessageId_FailsForNonExistentUser() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.setUserLastMessageId("test@test.com", 1);
	}
	
	@Test
	public void testSetUserLastMessageId_SucceedsForExistingUser() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.addUser("test@test.com", "Bill");
		chatRoomDAO.setUserLastMessageId("test@test.com", 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetPendingMessages_FailsForNull_EmailAddress() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.getPendingMessages(null);
	}


	@Test(expected=IllegalArgumentException.class)
	public void testGetPendingMessages_FailsForBlank_EmailAddress() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.getPendingMessages("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetPendingMessages_FailsForBlank_EmailAddress2() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.getPendingMessages("   ");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetPendingMessages_FailsForNonExistentUser() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		chatRoomDAO.getPendingMessages("test@test.com");
	}
	
	@Test
	public void testGetPendingMessages_ZeroMessgesInRepo_ExistingUser() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		String emailAddress = "test@test.com";
		chatRoomDAO.addUser(emailAddress, "Bill");
		chatRoomDAO.getPendingMessages(emailAddress);
	}
	
	@Test
	public void testGetPendingMessages_MessgesInRepo_GreaterThanSnapshotSize_ExistingUser() {
		String userEmailAddress = "test@test.com";
		String message = "Hey";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		int snapshotSize = 10;
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl(snapshotSize);
		chatRoomDAO.addUser(userEmailAddress, "Bill");
		for (; messageId <= (snapshotSize*2); messageId++) {
			chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
		}
		
		Map<Integer, Message> pendingMessages = chatRoomDAO.getPendingMessages(userEmailAddress);
		//Arrays.
		assertThat(pendingMessages.entrySet(), hasSize(snapshotSize));
	}

	@Test
	public void testGetPendingMessages_MessgesInRepo_LessThanSnapshotSize_ExistingUser() {
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl();
		String userEmailAddress = "test@test.com";
		String message = "Hey";
		int messageId = 1;
		int numTestMessages = 2;
		Date timestamp = Calendar.getInstance().getTime();
		
		chatRoomDAO.addUser(userEmailAddress, "Bill");
		for (; messageId <= (numTestMessages); messageId++) {
			chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
		}

		Map<Integer, Message> pendingMessages = chatRoomDAO.getPendingMessages(userEmailAddress);
		assertThat(pendingMessages.entrySet(), hasSize(numTestMessages));
	}

	@Test
	public void testGetPendingMessages_MessgesInRepo_EqualToSnapshotSize_ExistingUser() {
		String userEmailAddress = "test@test.com";
		String message = "Hey";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		int snapshotSize = 10;
		
		ChatRoom chatRoomDAO = new ChatRoomDAOImpl(snapshotSize);
		chatRoomDAO.addUser(userEmailAddress, "Bill");
		for (; messageId <= (snapshotSize); messageId++) {
			chatRoomDAO.addMessage(messageId, message, timestamp, userEmailAddress);
		}
		
		Map<Integer, Message> pendingMessages = chatRoomDAO.getPendingMessages(userEmailAddress);
		//Arrays.
		assertThat(pendingMessages.entrySet(), hasSize(snapshotSize));
	}

	
}
