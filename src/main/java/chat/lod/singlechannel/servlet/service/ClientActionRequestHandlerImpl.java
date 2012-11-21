package chat.lod.singlechannel.servlet.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.lod.singlechannel.dao.ChatRoomDAO;
import chat.lod.singlechannel.service.messaging.SnapshotMessengerService;
import chat.servlet.service.ClientActionRequestHandler;
import chat.util.ChatClientAction;

public class ClientActionRequestHandlerImpl implements
		ClientActionRequestHandler {
	
	private static final Log log = LogFactory.getLog(ClientActionRequestHandlerImpl.class);

	private ChatRoomDAO userManager;
	private SnapshotMessengerService messengerService;
	
	public ClientActionRequestHandlerImpl(ChatRoomDAO channelUserManager, 
											SnapshotMessengerService messengerService) {
		this.userManager = channelUserManager;
		this.messengerService = messengerService;
	}

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
			log.info("user=" + userEmailAddress + " - Poll request for session: " + request.getSession(true).getId());
			String lastMessageIdStr = request.getParameter("lastMessageId");
			if (lastMessageIdStr != null && !"".equals(lastMessageIdStr)) {
				int lastMessageId = Integer.parseInt(lastMessageIdStr);
				// We rely on the lastMessageId sent up from the client to achieve reliable messaging
				// http is unreliable so we
				userManager.setUserLastMessageId(userEmailAddress, lastMessageId);
				if (lastMessageId == -1) {
					messengerService.snapshot();
				}
			}
		}
	}
	
}
