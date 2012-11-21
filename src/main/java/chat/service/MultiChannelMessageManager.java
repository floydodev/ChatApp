package chat.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import chat.dao.ChannelDAO;
import chat.model.Channel;
import chat.model.ChatMessage;
import chat.model.User;

public class MultiChannelMessageManager {
	
	private ChannelDAO channelDAO;
	private boolean hasNewMessages = false;
	
	public MultiChannelMessageManager(ChannelDAO channelDAO) {
		this.channelDAO = channelDAO;
	}
	
	public void addMessage(String channelId, String message, Date date, User user) {
		hasNewMessages  = true;
		channelDAO.addMessage(channelId, message, date, user);
	}
	
	public Map<Integer, ChatMessage> getMessagesSince(String channelId, int lastMessageId) {
		return channelDAO.getMessagesSince(channelId, lastMessageId);
	}

	public Map<Integer, ChatMessage> getMessages(String channelId) {
		return channelDAO.getMessages(channelId);
	}

	public Map<Integer, ChatMessage> getLastXMessages(String channelId, int i) {
		Map<Integer, ChatMessage> messageMap = new HashMap<Integer, ChatMessage>();
		int lastMessageId = getChannel(channelId).getLastMessageId();
		// after this point getLastMessageId may still change before we do sinceMessageId 
		// calculation. So a local variable is used to avoid threading issues.
		// Any "missed" message will be picked up when client polls next time around
		// i.e. any incoming message that bumped "lastMessageId" whilst this code executed
		if (lastMessageId > 0) {
			int sinceMessageId = lastMessageId < 10 ? 0 : lastMessageId - 10;
			messageMap = channelDAO.getMessagesSince(channelId, sinceMessageId);
		}
		return messageMap;
	}
	
	public boolean hasNewMessages() {
		return hasNewMessages;
	}
	
	public void newMessagesProcessed() {
		this.hasNewMessages = false;
	}
	
	public Channel getChannel(String channelId) {
		return channelDAO.getChannel(channelId);
	}

}
