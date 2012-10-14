package chat.manager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.model.ChatMessage;
import chat.model.InMemoryChannelImpl;

public class ChannelMessageManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_NoMessagePresentInNewMgr() {
		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new InMemoryChannelImpl());
		assertEquals(0, channelMsgMgr.getMessages(-1).size());
	}
	
	@Test
	public void test_SingleMessageIsAdded() {
		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new InMemoryChannelImpl());
		channelMsgMgr.addMessage(new ChatMessage(null, null, null));
		assertEquals(1, channelMsgMgr.getMessages().size());
	}
	
	
	@Test
	public void test_MultipleMessagesAreAdded() {
		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new InMemoryChannelImpl());
		int expectedNumMessages = 5;
		for (int i = 1; i <= expectedNumMessages; i++) {
			channelMsgMgr.addMessage(new ChatMessage("message" + i, null, null));
		}
		assertEquals(expectedNumMessages, channelMsgMgr.getMessages().size());
	}


}
