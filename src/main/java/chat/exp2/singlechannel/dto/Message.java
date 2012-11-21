package chat.exp2.singlechannel.dto;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Message {

	private final static Log log = LogFactory.getLog(Message.class); 
	
	private String text;
	private Date timestamp;
	private User user;
	private int messageId;
	
	public Message(String text, Date timestamp, User user, int messageId) {
		this.text = text;
		this.timestamp = timestamp;
		this.user = user;
		this.messageId = messageId;
		log.info("Message created here with messageId=" + messageId); 
	}

//	public int getMessageId() {
//		return messageId;
//	}
//
//	public String getText() {
//		return text;
//	}
//
//	public Date getTimestamp() {
//		return timestamp;
//	}
//
//	public User getUser() {
//		return user;
//	}

	@Override
	public String toString() {
		return "[messageId=" + messageId + ", text=" + text + ", timestamp=" + timestamp
				+ ", user=" + user.getEmailAddress() + "]";
	}
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + messageId;
//		result = prime * result + ((text == null) ? 0 : text.hashCode());
//		result = prime * result
//				+ ((timestamp == null) ? 0 : timestamp.hashCode());
//		result = prime * result + ((user == null) ? 0 : user.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		ChatMessage other = (ChatMessage) obj;
//		if (messageId != other.messageId)
//			return false;
//		if (text == null) {
//			if (other.text != null)
//				return false;
//		} else if (!text.equals(other.text))
//			return false;
//		if (timestamp == null) {
//			if (other.timestamp != null)
//				return false;
//		} else if (!timestamp.equals(other.timestamp))
//			return false;
//		if (user == null) {
//			if (other.user != null)
//				return false;
//		} else if (!user.equals(other.user))
//			return false;
//		return true;
//	}	
}
