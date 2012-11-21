package chat.singlechannel.service;

import java.io.PrintWriter;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.singlechannel.dao.ChatRoomDAO;
import chat.util.MessageIdFountain;
import chat.util.MessageIdFountainAtomicIntImpl;

import com.google.gson.Gson;

public class MessageManager {
	
	private static final Log log = LogFactory.getLog(MessageManager.class);
	private ChatRoomDAO chatRoomDAO;
	private boolean hasNewMessages = false;
	private MessageIdFountain messageIdFountain = null;
	
//	public MessageManagerImpl(OldChatRoomIntf chatRoomDAO) {
//		//this.chatRoomDAO = chatRoomDAO;
//		this.messageIdFountain = new MessageIdFountainAtomicIntImpl();
//	}

	public MessageManager(ChatRoomDAO chatRoomDAO) {
		this.chatRoomDAO = chatRoomDAO;
		this.messageIdFountain = new MessageIdFountainAtomicIntImpl();
	}

	/* (non-Javadoc)
	 * @see chat.lod.singlechannel.service.MessageManagerIntf#addMessage(java.lang.String, java.util.Date, java.lang.String)
	 */
	public boolean addMessage(String message, Date date, String userEmailAddress) {
		hasNewMessages  = true;
		if (userEmailAddress == null || "".equals(userEmailAddress)) {
			log.error("Cannot add message: userEmailAddress is not present");
			return false;
		}
		if (message == null || "".equals(message)) {
			log.error("Cannot add message: messageText is not present");
			return false;
		}
		int id = messageIdFountain.getNextId();
		return chatRoomDAO.addMessage(message, date, userEmailAddress);
		// put(id, new ChatMessage(message, date, users.get(userEmailAddress), id)) != null;
		//chatRoomDAO.addMessage(messageIdFountain.getNextId(), message, date, userEmailAddress);
	}
	
	/* (non-Javadoc)
	 * @see chat.lod.singlechannel.service.MessageManagerIntf#hasNewMessages()
	 */
	public boolean hasNewMessages() {
		return hasNewMessages;
	}

	/* (non-Javadoc)
	 * @see chat.lod.singlechannel.service.MessageManagerIntf#hasMessages()
	 */
	public boolean hasMessages() {
		return chatRoomDAO.hasMessages();
	}
	
	/* (non-Javadoc)
	 * @see chat.lod.singlechannel.service.MessageManagerIntf#newMessagesProcessed()
	 */
	public void newMessagesProcessed() {
		this.hasNewMessages = false;
	}
//	
//	public void applyConnectionLogic() {
//		for (Map.Entry<String, PrintWriter> entry : userConnectionMap.entrySet()) {
//			pushPendingMessages(entry.getKey(), entry.getValue());
//		}
//	}
//	
	public void pushPendingMessages(String userEmailAddress, PrintWriter writer) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(chatRoomDAO.getPendingMessages(userEmailAddress));
		writer.println(jsonString);
		writer.flush();
		writer.close();
	}

//	public Map<Integer, Message> getMessagesSince(
//			int messageId) {
//		return chatRoomDAO.getMessagesSince(messageId);
//	}

}
