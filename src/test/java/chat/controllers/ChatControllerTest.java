package chat.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.dao.ChannelDAOInMemoryImpl;
import chat.manager.ChannelMessageManager;
import chat.model.Channel;
import chat.model.ChatMessage;
import chat.model.User;

public class ChatControllerTest {
	
	private final static Log log = LogFactory.getLog(ChatControllerTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void refactored_pollForMessages_ReturnsUpToTenMessagesForFirstTimeRequest() {
		ChannelMessageManager channelMsgMgr 
									= new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
		int expectedNumMessages = 10;
		int testMessages = 15;
		for (int i = 1; i <= testMessages; i++) {
			channelMsgMgr.addMessage(new ChatMessage("message" + i, null, null));
		}		

		// Assert a polled messageManager with more than 10 messages returns only 10
		ChatController chatController = new ChatController();
		chatController.setChannelManager(channelMsgMgr);
		Map<Integer, ChatMessage> resultMap = chatController.pollForMessages_New(-1, new User());
		assertEquals(expectedNumMessages, resultMap.size());
		
		// Restart test with a fresh, empty ChatMessageManager map
		channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
		chatController.setChannelManager(channelMsgMgr);
	
		resultMap = chatController.pollForMessages_New(-1, new User());
		// Assert a polled empty messageManager returns zero messages
		assertEquals(0, resultMap.size());
		
		// Assert a polled messageManager with less than 10 messages returns only what's there.
		testMessages = 5;
		for (int i = 1; i <= testMessages; i++) {
			channelMsgMgr.addMessage(new ChatMessage("message" + i, null, null));
		}
		resultMap = chatController.pollForMessages_New(-1, new User());
		assertEquals(testMessages, resultMap.size());
	}	
	
	
	@Test
	public void refactored_processReceivedMessage_StoresMessage_ReturnsView() {
		ChannelMessageManager channelMsgMgr 
									= new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
		ChatController chatController = new ChatController();
		chatController.setChannelManager(channelMsgMgr);
		assertEquals(0, channelMsgMgr.getMessagesSince(-1).size());
		assertEquals(0, channelMsgMgr.getMessages().size());
		
		String expectedMessage = "test message";
		String retVal = chatController.processReceivedMessage_New(expectedMessage, new User());
		Integer messageKey = channelMsgMgr.getMessages().keySet().iterator().next();
		ChatMessage chatMessage = channelMsgMgr.getMessages().get(messageKey);
		assertEquals(expectedMessage, chatMessage.getText());
		assertEquals("chatHome", retVal);
	}


	@Test
	public void refactored_pollForMessages_FirstTimeCall_MessagesPending() {
				
		int lastMessageId = -1;
		User user = new User("Test Display Name", "test@test.com");
		int numTestMessages = 5;
		String textRoot = "test message:";

		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
		ChatController chatController = new ChatController();
		chatController.setChannelManager(channelMsgMgr);
		
		for (int i = 1; i <= numTestMessages; i++) {
			chatController.processReceivedMessage(textRoot + i, user);
		}
		
		Map<Integer, ChatMessage> actualMessageMap = chatController.pollForMessages_New(lastMessageId, user);
		
		// numTestMessages expected
		assertEquals(numTestMessages, actualMessageMap.size());

		// Check all messages have same contents
		// Tricky to use equals because of the generated ids Should get easier when id management is moved to a separate class
		int count = 1;
		for (Integer key : actualMessageMap.keySet()) {
			ChatMessage actualMessage = actualMessageMap.get(key);
			// Compare text content of message. MessageIds will be different..
			// relies on keySet following natural order
			assertEquals(textRoot + (count++), actualMessage.getText());
			// Compare User associated with the message
			assertEquals(user, actualMessage.getUser());
		}
		
	}
	
	
	@Test
	public void refactored_pollForMessages_FirstTimeCall_ZeroMessagesPending() {
				
		User user = new User("Test Display Name", "test@test.com");

		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
		ChatController chatController = new ChatController();
		chatController.setChannelManager(channelMsgMgr);
		
		// Not a thread safe test due to reliance on global id in ChatMessage
		Map<Integer, ChatMessage> actualMessageMap = chatController.pollForMessages_New(ChatMessage.lastMessageId(), user);
		
		// numTestMessages expected
		assertEquals(0, actualMessageMap.size());

		// Check all messages have same contents
	}
	
	
	@Test
	public void refactored_pollForMessages_SecondTimeCall_MessagesPending() {
				
		User user = new User("Test Display Name", "test@test.com");
		int numTestMessages = 15;
		int lastMessageIdReceived = 0;
		String textRoot = "test message:";

		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
		ChatController chatController = new ChatController();
		chatController.setChannelManager(channelMsgMgr);
		
		for (int i = 1; i <= numTestMessages; i++) {
			chatController.processReceivedMessage(textRoot + i, user);
		}
		
		//int lastMessageId = ChatMessage.lastMessageId();
		
		Map<Integer, ChatMessage> actualMessageMap = chatController.pollForMessages_New(lastMessageIdReceived, user);
		
		// numTestMessages expected
		assertEquals(numTestMessages - lastMessageIdReceived, actualMessageMap.size());

		// Check all messages have same contents
		// Tricky to use equals because of the generated ids Should get easier when id management is moved to a separate class
	}


//	@Test
//	public void pollForMessages_ReturnsUpToTenMessagesForFirstTimeRequest() {
//		ChannelMessageManager channelMsgMgr 
//									= new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
//		int expectedNumMessages = 10;
//		int testMessages = 15;
//		for (int i = 1; i <= testMessages; i++) {
//			channelMsgMgr.addMessage(new ChatMessage("message" + i, null, null));
//		}		
//
//		// Assert a polled messageManager with more than 10 messages returns only 10
//		ChatController chatController = new ChatController();
//		chatController.setChannelManager(channelMsgMgr);
//		Map<Integer, ChatMessage> resultMap = chatController.pollForMessages(-1, new User());
//		assertEquals(expectedNumMessages, resultMap.size());
//		
//		// Restart test with a fresh, empty ChatMessageManager map
//		channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
//		chatController.setChannelManager(channelMsgMgr);
//	
//		resultMap = chatController.pollForMessages(-1, new User());
//		// Assert a polled empty messageManager returns zero messages
//		assertEquals(0, resultMap.size());
//		
//		// Assert a polled messageManager with less than 10 messages returns only what's there.
//		testMessages = 5;
//		for (int i = 1; i <= testMessages; i++) {
//			channelMsgMgr.addMessage(new ChatMessage("message" + i, null, null));
//		}
//		resultMap = chatController.pollForMessages(-1, new User());
//		assertEquals(testMessages, resultMap.size());
//	}
	
//	@Test
//	public void processReceivedMessage_StoresMessage_ReturnsView() {
//		ChannelMessageManager channelMsgMgr 
//									= new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
//		ChatController chatController = new ChatController();
//		chatController.setChannelManager(channelMsgMgr);
//		assertEquals(0, channelMsgMgr.getMessagesSince(-1).size());
//		assertEquals(0, channelMsgMgr.getMessages().size());
//		
//		String expectedMessage = "test message";
//		String retVal = chatController.processReceivedMessage(expectedMessage, new User());
//		Integer messageKey = channelMsgMgr.getMessages().keySet().iterator().next();
//		ChatMessage chatMessage = channelMsgMgr.getMessages().get(messageKey);
//		assertEquals(expectedMessage, chatMessage.getText());
//		assertEquals("chatHome", retVal);
//	}	
	
//	@Test
//	public void pollForMessages_FirstTimeCall_MessagesPending() {
//				
//		int lastMessageId = -1;
//		User user = new User("Test Display Name", "test@test.com");
//		int numTestMessages = 5;
//		String textRoot = "test message:";
//
//		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
//		ChatController chatController = new ChatController();
//		chatController.setChannelManager(channelMsgMgr);
//		
//		for (int i = 1; i <= numTestMessages; i++) {
//			chatController.processReceivedMessage(textRoot + i, user);
//		}
//		
//		Map<Integer, ChatMessage> actualMessageMap = chatController.pollForMessages(lastMessageId, user);
//		
//		// numTestMessages expected
//		assertEquals(numTestMessages, actualMessageMap.size());
//
//		// Check all messages have same contents
//		// Tricky to use equals because of the generated ids Should get easier when id management is moved to a separate class
//		int count = 1;
//		for (Integer key : actualMessageMap.keySet()) {
//			ChatMessage actualMessage = actualMessageMap.get(key);
//			// Compare text content of message. MessageIds will be different..
//			// relies on keySet following natural order
//			assertEquals(textRoot + (count++), actualMessage.getText());
//			// Compare User associated with the message
//			assertEquals(user, actualMessage.getUser());
//		}
//		
//	}
//	
//	
//	@Test
//	public void pollForMessages_FirstTimeCall_ZeroMessagesPending() {
//				
//		User user = new User("Test Display Name", "test@test.com");
//
//		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
//		ChatController chatController = new ChatController();
//		chatController.setChannelManager(channelMsgMgr);
//		
//		// Not a thread safe test due to reliance on global id in ChatMessage
//		Map<Integer, ChatMessage> actualMessageMap = chatController.pollForMessages(ChatMessage.lastMessageId(), user);
//		
//		// numTestMessages expected
//		assertEquals(0, actualMessageMap.size());
//
//		// Check all messages have same contents
//	}
//
//	
//	@Test
//	public void pollForMessages_SecondTimeCall_MessagesPending() {
//				
//		User user = new User("Test Display Name", "test@test.com");
//		int numTestMessages = 15;
//		int lastMessageIdReceived = 0;
//		String textRoot = "test message:";
//
//		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
//		ChatController chatController = new ChatController();
//		chatController.setChannelManager(channelMsgMgr);
//		
//		for (int i = 1; i <= numTestMessages; i++) {
//			chatController.processReceivedMessage(textRoot + i, user);
//		}
//		
//		//int lastMessageId = ChatMessage.lastMessageId();
//		
//		Map<Integer, ChatMessage> actualMessageMap = chatController.pollForMessages(lastMessageIdReceived, user);
//		
//		// numTestMessages expected
//		assertEquals(numTestMessages - lastMessageIdReceived, actualMessageMap.size());
//
//		// Check all messages have same contents
//		// Tricky to use equals because of the generated ids Should get easier when id management is moved to a separate class
//	}

}
