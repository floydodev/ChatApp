package chat.manager;

import java.util.HashMap;
import java.util.Map;

import chat.model.Channel;
import chat.model.ChatMessage;

public class ChannelMessageManager {
	
	private Map<String, Channel> channelMap = new HashMap<String, Channel>();
	
	public ChannelMessageManager(Channel channel) {
		channelMap.put(channel.getName(), channel);
	}
	
	public void addMessage(ChatMessage chatMessage) {
		addMessage(Channel.defaultName, chatMessage);
	}

	public void addMessage(String channelId, ChatMessage chatMessage) {
		channelMap.get(channelId).addMessage(chatMessage);
	}
	
	public Map<Integer, ChatMessage> getMessages(int lastMessageId) {
		return getMessages(lastMessageId, Channel.defaultName);
	}
	
	public Map<Integer, ChatMessage> getMessages(int lastMessageId, String channelId) {
		return channelMap.get(channelId).getMessages(lastMessageId);
	}
	
	public Map<Integer, ChatMessage> getMessages() {
		return getMessages(Channel.defaultName);
	}
	
	public Map<Integer, ChatMessage> getMessages(String channelId) {
		return channelMap.get(channelId).getMessagesMap();
	}
	
}
