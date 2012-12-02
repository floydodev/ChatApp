package chat.singlechannel.service;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.multichannel.model.ChatMessage;
import chat.singlechannel.dao.ChatRoom;
import chat.singlechannel.dto.Message;
import chat.util.MessageIdFountain;
import chat.util.MessageIdFountainAtomicIntImpl;

import com.google.gson.Gson;

public class MessageManager {
	
	private static final Log log = LogFactory.getLog(MessageManager.class);
	private ChatRoom chatRoomDAO;
	private boolean hasNewMessages = false;
	private boolean snapshotRequested = false;
	private MessageIdFountain messageIdFountain = null;
	
//	public MessageManagerImpl(OldChatRoomIntf chatRoomDAO) {
//		//this.chatRoomDAO = chatRoomDAO;
//		this.messageIdFountain = new MessageIdFountainAtomicIntImpl();
//	}

	public MessageManager(ChatRoom chatRoomDAO) {
		this.chatRoomDAO = chatRoomDAO;
		this.messageIdFountain = new MessageIdFountainAtomicIntImpl();
	}

	/* (non-Javadoc)
	 * @see chat.lod.singlechannel.service.MessageManagerIntf#addMessage(java.lang.String, java.util.Date, java.lang.String)
	 */
	public void addMessage(String message, Date date, String userEmailAddress) {
		hasNewMessages = true;
		chatRoomDAO.addMessage(messageIdFountain.getNextId(), message, date, userEmailAddress);
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
	
	public void snapshotRequest() {
		snapshotRequested = true;
	}
	
	public boolean snapshotRequested() {
		return snapshotRequested;
	}
	
	public void snapshotRequestComplete() {
		snapshotRequested = false;
	}
//	
//	public void applyConnectionLogic() {
//		for (Map.Entry<String, PrintWriter> entry : userConnectionMap.entrySet()) {
//			pushPendingMessages(entry.getKey(), entry.getValue());
//		}
//	}
//	
	public void pushPendingMessages(String userEmailAddress, PrintWriter writer) {
		NavigableMap<Integer, Message> pendingMessages = chatRoomDAO.getPendingMessages(userEmailAddress);
		if (pendingMessages == null || pendingMessages.isEmpty()) {
			return;
		}
		Gson gson = new Gson();
		String jsonString = gson.toJson(pendingMessages);
		writer.println(jsonString);
		writer.flush();
		writer.close();
		for (Map.Entry <Integer, Message> entry : pendingMessages.entrySet()) {
			log.info("user=" + userEmailAddress + " - Sent: " + entry.getValue());
		}
	}

//	public Map<Integer, Message> getMessagesSince(
//			int messageId) {
//		return chatRoomDAO.getMessagesSince(messageId);
//	}

}
