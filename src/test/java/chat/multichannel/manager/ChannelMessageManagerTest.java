package chat.multichannel.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.multichannel.dao.ChannelDAOInMemoryImpl;
import chat.multichannel.model.Channel;
import chat.multichannel.model.ChatMessage;
import chat.multichannel.model.User;
import chat.multichannel.service.ChannelMessageManager;

public class ChannelMessageManagerTest {
	
	private ChannelMessageManager channelMsgMgr; 

	@Before
	public void setUp() throws Exception {
		channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl(new Channel()));
	}

	@After
	public void tearDown() throws Exception {
		channelMsgMgr = null;
	}

	@Test
	public void test_NoMessagePresentInNewMgr() {

		assertEquals(0, channelMsgMgr.getMessagesSince(-1).size());
	}
	
	@Test
	public void test_SingleMessageIsAddedAndStored() {
		String testText = "hey there";
		User testUser = new User("Mr. Test", "tester@test.com");
		ChatMessage testChatMessage = new ChatMessage(testText, Calendar.getInstance().getTime(), testUser, 0);
		
		assertEquals(0, channelMsgMgr.getMessages().size());
		
		// Assert message contents match
		channelMsgMgr.addMessage(testChatMessage.getText(), testChatMessage.getTimestamp(), testChatMessage.getUser());
		assertEquals(1, channelMsgMgr.getMessages().size());
		for (Integer key : channelMsgMgr.getMessages().keySet()) {
			ChatMessage chatMessage = channelMsgMgr.getMessages().get(key);
			assertEquals(testChatMessage.getText(), chatMessage.getText());
			assertEquals(testChatMessage.getTimestamp(), chatMessage.getTimestamp());
			assertEquals(testChatMessage.getUser(), chatMessage.getUser());
		}
	}


	@Test
	public void test_MultipleMessagesAreAdded() {

		int expectedNumMessages = 5;
		for (int i = 1; i <= expectedNumMessages; i++) {
			channelMsgMgr.addMessage("message" + i, Calendar.getInstance().getTime(), null);
		}
		assertEquals(expectedNumMessages, channelMsgMgr.getMessages().size());
	}


}
