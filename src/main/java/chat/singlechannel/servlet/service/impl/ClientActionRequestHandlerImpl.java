package chat.singlechannel.servlet.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.singlechannel.dao.ChatRoom;
import chat.singlechannel.service.messaging.SnapshotMessengerService;
import chat.singlechannel.servlet.service.ClientActionRequestHandler;
import chat.util.ChatClientAction;

public class ClientActionRequestHandlerImpl implements ClientActionRequestHandler {
	
	private static final Log log = LogFactory.getLog(ClientActionRequestHandlerImpl.class);

	private ChatRoom userManager;
	private SnapshotMessengerService messengerService;
	
	public ClientActionRequestHandlerImpl(ChatRoom channelUserManager, 
											SnapshotMessengerService messengerService) {
		this.userManager = channelUserManager;
		this.messengerService = messengerService;
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.servlet.service.ClientActionRequestHandler#handle(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	public void handle(HttpServletRequest request, String action) {
		String userEmailAddress = (String)request.getSession(true).getAttribute("user");

		if (ChatClientAction.SEND.equals(action)) {
			log.info("user=" + userEmailAddress + " - Chat request for session: " + request.getSession(true).getId());
			String chatMessageStr = request.getParameter("chatMessage");
			if (chatMessageStr != null && !"".equals(chatMessageStr)) {
				messengerService.consume(chatMessageStr, userEmailAddress);
			} else {
				log.info("chatMessageStr=<blank>");
			}
		}
		else if (ChatClientAction.POLL.equals(action)) {
			log.info("user=" + userEmailAddress + " - Poll request received for session: " + request.getSession(true).getId());
			String lastMessageIdStr = request.getParameter("lastMessageId");
			if (lastMessageIdStr != null && !"".equals(lastMessageIdStr)) {
				int lastMessageId = Integer.parseInt(lastMessageIdStr);
				// We use on the lastMessageId sent up from the client to achieve reliable messaging
				userManager.setUserLastMessageId(userEmailAddress, lastMessageId);
			}
		}
		else if (ChatClientAction.SNAPSHOT.equals(action)) {
			log.info("user=" + userEmailAddress + " - Snapshot request for session: " + request.getSession(true).getId());
			messengerService.snapshot();
		}
	}
	
}
