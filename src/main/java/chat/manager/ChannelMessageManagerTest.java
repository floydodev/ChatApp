package chat.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.model.ChannelDAOInMemoryImpl;
import chat.model.ChatMessage;
import chat.model.User;

public class ChannelMessageManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_NoMessagePresentInNewMgr() {
		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl());
		assertEquals(0, channelMsgMgr.getMessages(-1).size());
	}
	
	@Test
	public void test_SingleMessageIsAddedAndStored() {
		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl());
		String testText = "hey there";
		User testUser = new User("Mr. Test", "tester@test.com");
		ChatMessage testChatMessage = new ChatMessage(testText, Calendar.getInstance().getTime(), testUser);
		
		assertEquals(0, channelMsgMgr.getMessages().size());
		
		channelMsgMgr.addMessage(testChatMessage);
		assertEquals(1, channelMsgMgr.getMessages().size());
		assertTrue(channelMsgMgr.getMessages().containsValue(testChatMessage));
	}


	@Test
	public void test_MultipleMessagesAreAdded() {
		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new ChannelDAOInMemoryImpl());
		int expectedNumMessages = 5;
		for (int i = 1; i <= expectedNumMessages; i++) {
			channelMsgMgr.addMessage(new ChatMessage("message" + i, null, null));
		}
		assertEquals(expectedNumMessages, channelMsgMgr.getMessages().size());
	}


}
