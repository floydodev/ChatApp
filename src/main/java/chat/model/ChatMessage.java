package chat.model;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatMessage {

	@Override
	public String toString() {
		return "ChatMessage [text=" + text + ", timestamp=" + timestamp
				+ ", user=" + user + ", messageId=" + messageId + "]";
	}

	private String text;
	private Date timestamp;
	private User user;
	private int messageId;
	private static AtomicInteger messageIdFountain = new AtomicInteger();
	
	public ChatMessage(String text, Date timestamp, User user) {
		super();
		this.text = text;
		this.timestamp = timestamp;
		this.user = user;
		this.messageId = messageIdFountain.incrementAndGet();
	}
	
	public static int lastMessageId() {
		return messageIdFountain.get();
	}

	public int getMessageId() {
		return messageId;
	}

	public String getText() {
		return text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public User getUser() {
		return user;
	}

	public static AtomicInteger getMessageIdFountain() {
		return messageIdFountain;
	}
	
}
