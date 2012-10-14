package chat.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.manager.ChannelMessageManager;
import chat.model.ChatMessage;
import chat.model.InMemoryChannelImpl;

public class ChatControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_ReturnsOnlyTenMessagesForFirstTimeRequest() {
		ChannelMessageManager channelMsgMgr = new ChannelMessageManager(new InMemoryChannelImpl());
		int expectedNumMessages = 10;
		int testMessages = 15;
		int lastMessageId = -1; // Means client has received no messages so far
		for (int i = 1; i <= testMessages; i++) {
			channelMsgMgr.addMessage(new ChatMessage("message" + i, null, null));
		}		

		ChatController chatController = new ChatController();
		chatController.setChannelManager(channelMsgMgr);
		Map<Integer, ChatMessage> resultMap = chatController.processFirstTimePoll(null);
		assertEquals(expectedNumMessages, resultMap.size());
	}

}
