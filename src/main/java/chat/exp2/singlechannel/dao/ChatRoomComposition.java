package chat.exp2.singlechannel.dao;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.exp2.singlechannel.dto.Message;
import chat.exp2.singlechannel.dto.User;
import chat.exp2.singlechannel.service.MessageManager;
import chat.exp2.singlechannel.service.MessagePusher;
import chat.exp2.singlechannel.service.UserManager;
import chat.util.MessageIdFountain;

import com.google.gson.Gson;


public class ChatRoomComposition {
//	implements MessageManager, UserDAO, MessagePusher {
//}
	
	private MessageDAO messageDAO;
	private UserDAO userDAO;
	private boolean hasNewMessages = false;
	private MessagePusher messagePusher;
	//private NavigableMap<Integer, ChatMessage> messages;
	//private Map<String, User> users;
	private final static Log log = LogFactory.getLog(ChatRoomComposition.class);
	//private MessageIdFountain messageIdFountain = null;
	
	public ChatRoomComposition(MessageDAO messageDAO, UserDAO userDAO,
								MessagePusher messagePusher) {
		this.messageDAO = messageDAO;
		this.userDAO = userDAO;
		this.messagePusher = messagePusher;
	}

	public boolean addMessage(String message, Date date, String userEmailAddress) {
		return messageDAO.addMessage(message, date, userDAO.getUser(userEmailAddress));
	}

	public void addUser(String emailAddress, String displayName) {
		userDAO.addUser(emailAddress, displayName);
	}

	public boolean removeUser(String emailAddress) {
		return userDAO.removeUser(emailAddress);
	}
	
	public boolean hasMessages() {
		return messageDAO.hasMessages();
	}

	public void setUserLastMessageId(String userEmailAddress, int lastMessageId) {
		userDAO.setUserLastMessageId(userEmailAddress, lastMessageId);
	}

	public boolean hasUser(String userEmailAddress) {
		return userDAO.hasUser(userEmailAddress);
	}

//	public void pushPendingMessages(String userEmailAddress, PrintWriter writer) {
//		messagePusher.pushPendingMessages(userEmailAddress, writer, getPendingMessages(userEmailAddress));
//	}
	
	public boolean hasNewMessages() {
		return hasNewMessages;
	}
	
	public void newMessagesProcessed() {
		this.hasNewMessages = false;
	}

	public int getUserLastMessageId(String userEmailAddress) {
		return userDAO.getUserLastMessageId(userEmailAddress);
	}

	public void pushPendingMessages(String userEmailAddress, PrintWriter writer) {
		messagePusher.pushPendingMessages(userEmailAddress, writer);
	}

	public Map<Integer, Message> getMessagesSince(int messageId) {
		return messageDAO.getMessagesSince(messageId);
	}
	
	private Map<Integer, Message> getPendingMessages(String userEmailAddress) {
		return messageDAO.getMessagesSince(userDAO.getUserLastMessageId(userEmailAddress));
	}

}
