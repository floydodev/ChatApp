package chat.manager;

import java.util.HashMap;
import java.util.Map;

import chat.model.ChannelDAO;
import chat.model.ChatMessage;

public class ChannelMessageManager {
	
	private Map<String, ChannelDAO> channelMap = new HashMap<String, ChannelDAO>();
	
	public ChannelMessageManager(ChannelDAO channel) {
		channelMap.put(channel.getName(), channel);
	}
	
	public void addMessage(ChatMessage chatMessage) {
		addMessage(ChannelDAO.defaultName, chatMessage);
	}

	public void addMessage(String channelId, ChatMessage chatMessage) {
		channelMap.get(channelId).addMessage(chatMessage);
	}
	
	public Map<Integer, ChatMessage> getMessages(int lastMessageId) {
		return getMessages(lastMessageId, ChannelDAO.defaultName);
	}
	
	public Map<Integer, ChatMessage> getMessages(int lastMessageId, String channelId) {
		return channelMap.get(channelId).getMessages(lastMessageId);
	}
	
	public Map<Integer, ChatMessage> getMessages() {
		return getMessages(ChannelDAO.defaultName);
	}
	
	public Map<Integer, ChatMessage> getMessages(String channelId) {
		return channelMap.get(channelId).getMessagesMap();
	}
	
}
