package chat.singlechannel.dao;

//import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import chat.singlechannel.dto.Message;
import chat.singlechannel.dto.User;

@Ignore
public class ChatRoomImplTest {
	
	//@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_NonExistent_User() {
		String userEmailAddress = "test@test.com";
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		// Stub out a UsersDAO object
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(false);
		//User user = new User(userEmailAddress, "Mr Test");
		// Setup the expected results
		//Message expectedMessage = new Message(messageId, message, timestamp, user);

		// Test the addMessage method
		ChatRoom chatRoom = new ChatRoomImpl(null, usersDAO);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
		// Should throw an exception
	}
	
	@Test
	public void addMessage_SucceedsFor_Existing_User() {
		String userEmailAddress = "test@test.com";
		String displayName = "Bungalow Bill";
		String message = "Hey";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		
		// Pass in real User object as it is used in equals method of Message
		// Can't use Mockito stub object for this reason.
//		User user = new User(userEmailAddress, displayName);

		User user = mock(User.class);

		// Setup the expected results
		Message expectedMessage = new Message(messageId, message, timestamp, user);
		
		// Stub out a UsersDAO object
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(true);
		when(usersDAO.getLastMessageId(userEmailAddress)).thenReturn(1);
		when(usersDAO.get(userEmailAddress)).thenReturn(user);

		// Stub out a MessagesDAO object
		MessagesDAO messagesDAO = mock(MessagesDAO.class);
		NavigableMap<Integer, Message> resultMap = new TreeMap<Integer, Message>();
		resultMap.put(1, expectedMessage);
		when(messagesDAO.tailMap(messageId)).thenReturn(resultMap);

		// Test the addMessage method
		ChatRoom chatRoom = new ChatRoomImpl(messagesDAO, usersDAO);
		//chatRoom.addUser(user.getEmailAddress(), user.getDisplayName());
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
		// Get persisted messages
		Map<Integer, Message> pendingMessages = chatRoom.getPendingMessages(userEmailAddress);

		assertThat(pendingMessages, hasValue(expectedMessage));
		assertThat(pendingMessages.entrySet(), hasSize(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_Blank_EmailAddress() {
		String userEmailAddress = "";
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_Blank_EmailAddress2() {
		String userEmailAddress = "          ";
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_Null_EmailAddress() {
		String userEmailAddress = null;
		String message = "Hey this is a test";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_Null_MessageText() {
		String userEmailAddress = "test@test.com";;
		String message = null;
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_Blank_MessageText() {
		String userEmailAddress = "test@test.com";;
		String message = "";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_Blank_MessageText2() {
		String userEmailAddress = "test@test.com";;
		String message = "     ";
		int messageId = 1;
		Date timestamp = Calendar.getInstance().getTime();
		
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void addMessage_FailsFor_Null_Timestamp() {
		String userEmailAddress =  "test@test.com";;
		String message = "Test";
		int messageId = 1;
		Date timestamp = null;
		
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addMessage(messageId, message, timestamp, userEmailAddress);
	}
	
	@Test
	public void hasMessages_FailsWhen_Empty() {
		// Stub out the dependencies
		MessagesDAO messagesDAO = mock(MessagesDAO.class);
		when(messagesDAO.isEmpty()).thenReturn(true);
		
		ChatRoom chatRoom = new ChatRoomImpl(messagesDAO, null);

		// Test the hasMessages method
		assertFalse(chatRoom.hasMessages());
	}
	
	
	
	@Test
	public void hasMessages_SucceedsWhen_Populated() {
		// Stub out the dependencies
		MessagesDAO messagesDAO = mock(MessagesDAO.class);
		when(messagesDAO.isEmpty()).thenReturn(false);
		
		ChatRoom chatRoom = new ChatRoomImpl(messagesDAO, null);
		assertTrue(chatRoom.hasMessages());
	}

	@Test(expected=IllegalArgumentException.class)
	public void addUser_FailsForBlank_UserEmailAddress() {
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addUser("", "Bill");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addUser_FailsFor_Null_EmailAddess() {
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addUser(null, "Bill");

	}
	

	@Test(expected=IllegalArgumentException.class)
	public void addUser_FailsFor_Blank_DisplayName2() {
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addUser("test@test.com", "   ");
	}

	@Test(expected=IllegalArgumentException.class)
	public void addUser_FailsFor_Null_DisplayName() {
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.addUser("test@test.com", null);

	}
	
	@Test
	public void addUser_SucceedsFor_Valid_Args() {
		String userEmailAddress = "test@test.com";
		UsersDAO usersDAO = mock(UsersDAO.class);
		User user = mock(User.class);
		doNothing().when(usersDAO).put(userEmailAddress, user);
		
		ChatRoom chatRoom = new ChatRoomImpl(null, usersDAO);
		chatRoom.addUser(userEmailAddress, "Bill");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setUserLastMessageId_FailsForNonExistentUser() {
		String userEmailAddress = "test@test.com";
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(false);
		ChatRoom chatRoom = new ChatRoomImpl(null, usersDAO);
		chatRoom.setUserLastMessageId(userEmailAddress, 1);
	}
	
	@Test
	public void setUserLastMessageId_SucceedsForExistingUser() {
		String userEmailAddress = "test@test.com";
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(true);
		ChatRoom chatRoom = new ChatRoomImpl(null, usersDAO);
		chatRoom.setUserLastMessageId(userEmailAddress, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void getPendingMessages_FailsForNull_EmailAddress() {
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.getPendingMessages(null);
	}


	@Test(expected=IllegalArgumentException.class)
	public void getPendingMessages_FailsForBlank_EmailAddress() {
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.getPendingMessages("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getPendingMessages_FailsForBlank_EmailAddress2() {
		ChatRoom chatRoom = new ChatRoomImpl(null, null);
		chatRoom.getPendingMessages("   ");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getPendingMessages_FailsForNonExistentUser() {
		String userEmailAddress = "test@test.com";
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(false);
		ChatRoom chatRoom = new ChatRoomImpl(null, usersDAO);
		chatRoom.getPendingMessages(userEmailAddress);
	}
	
	@Test
	public void getPendingMessages_ZeroMessagesInRepo_ExistingUser() {
		String userEmailAddress = "test@test.com";
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(true);
		
		MessagesDAO messagesDAO = mock(MessagesDAO.class);
		when(messagesDAO.isEmpty()).thenReturn(true);
		ChatRoom chatRoom = new ChatRoomImpl(messagesDAO, usersDAO);

		//chatRoom.addUser(emailAddress, "Bill");
		chatRoom.getPendingMessages(userEmailAddress);
	}
	
	@Test
	public void getPendingMessages_MessagesInRepo_GreaterThanSnapshotSize_ExistingUser() {
		String userEmailAddress = "test@test.com";
		int repoSize = 10;
		int snapshotSize = 10;
		int lastMessageId = 0;
		int messagesSinceId = repoSize - snapshotSize;
		
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(true);
		when(usersDAO.getLastMessageId(userEmailAddress)).thenReturn(lastMessageId);
		
		NavigableMap<Integer, Message> pendingMessages = new TreeMap<Integer, Message>();
		for (int i = 1; i <= snapshotSize; i++) {
			pendingMessages.put(i, null);
		}

		MessagesDAO messagesDAO = mock(MessagesDAO.class);
		when(messagesDAO.isEmpty()).thenReturn(false);  
		when(messagesDAO.lastKey()).thenReturn(repoSize);
		when(messagesDAO.tailMap(messagesSinceId)).thenReturn(pendingMessages);
		
		ChatRoom chatRoom = new ChatRoomImpl(snapshotSize, messagesDAO, usersDAO);
		
		assertThat(chatRoom.getPendingMessages(userEmailAddress).entrySet(), hasSize(snapshotSize));
	}

	@Test
	public void getPendingMessages_MessagesInRepo_LessThanSnapshotSize_ExistingUser() {
		String userEmailAddress = "test@test.com";
		int repoSize = 5;
		int snapshotSize = 10;
		int lastMessageId = 0;
		int messagesSinceId = 0;
		
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(true);
		when(usersDAO.getLastMessageId(userEmailAddress)).thenReturn(lastMessageId);
		
		NavigableMap<Integer, Message> pendingMessages = new TreeMap<Integer, Message>();
		for (int i = 1; i <= snapshotSize; i++) {
			pendingMessages.put(i, null);
		}

		MessagesDAO messagesDAO = mock(MessagesDAO.class);
		when(messagesDAO.isEmpty()).thenReturn(false);  
		when(messagesDAO.lastKey()).thenReturn(repoSize);
		when(messagesDAO.tailMap(messagesSinceId)).thenReturn(pendingMessages);
		
		ChatRoom chatRoom = new ChatRoomImpl(snapshotSize, messagesDAO, usersDAO);
		
		assertThat(chatRoom.getPendingMessages(userEmailAddress).entrySet(), hasSize(snapshotSize));
	}

	@Test
	public void getPendingMessages_MessagesInRepo_EqualToSnapshotSize_ExistingUser() {
		String userEmailAddress = "test@test.com";
		int repoSize = 10;
		int snapshotSize = 10;
		int lastMessageId = 0;
		int messagesSinceId = 0;
		
		UsersDAO usersDAO = mock(UsersDAO.class);
		when(usersDAO.containsKey(userEmailAddress)).thenReturn(true);
		when(usersDAO.getLastMessageId(userEmailAddress)).thenReturn(lastMessageId);
		
		NavigableMap<Integer, Message> pendingMessages = new TreeMap<Integer, Message>();
		for (int i = 1; i <= snapshotSize; i++) {
			pendingMessages.put(i, null);
		}

		MessagesDAO messagesDAO = mock(MessagesDAO.class);
		when(messagesDAO.isEmpty()).thenReturn(false);  
		when(messagesDAO.lastKey()).thenReturn(repoSize);
		when(messagesDAO.tailMap(messagesSinceId)).thenReturn(pendingMessages);
		
		ChatRoom chatRoom = new ChatRoomImpl(snapshotSize, messagesDAO, usersDAO);
		
		assertThat(chatRoom.getPendingMessages(userEmailAddress).entrySet(), hasSize(snapshotSize));
	}

	
}
