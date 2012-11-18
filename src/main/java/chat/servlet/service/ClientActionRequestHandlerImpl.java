package chat.servlet.service;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.model.ChatMessage;
import chat.model.User;
import chat.service.ChannelUserManager;
import chat.service.MessengerService;
import chat.util.ChatClientAction;

public class ClientActionRequestHandlerImpl implements
		ClientActionRequestHandler {
	
	private static final Log log = LogFactory.getLog(ClientActionRequestHandlerImpl.class);

	private ChannelUserManager channelUserManager;
	//private ChannelMessageManager channelMessageManager;
	private MessengerService messengerService;
	
	public ClientActionRequestHandlerImpl(ChannelUserManager channelUserManager, MessengerService messengerService) {
		this.channelUserManager = channelUserManager;
		this.messengerService = messengerService;
	}
	
	public void handle(HttpServletRequest request, String action) {

		if (ChatClientAction.SEND.equals(action)) {
			log.info("Handle chat request");
			String chatMessageStr = request.getParameter("chatMessage");
			if (chatMessageStr != null && !"".equals(chatMessageStr)) {
				log.info("chatMessageStr=" + chatMessageStr);
				String userEmailAddress = (String)request.getSession(true).getAttribute("user");
				User user = channelUserManager.getUser(userEmailAddress);
				//channelMessageManager.addMessage(chatMessageStr, Calendar.getInstance().getTime(), user);
				ChatMessage chatMessage = 
						new ChatMessage(chatMessageStr, Calendar.getInstance().getTime(), user, ++messageId);
				messengerService.consume(chatMessage);
			} else {
				log.info("chatMessageStr=<blank>");
			}
		}
		else if (ChatClientAction.POLL.equals(action)) {
			log.info("Handle polling request");
			String lastMessageIdStr = request.getParameter("lastMessageId");
			if (lastMessageIdStr != null && !"".equals(lastMessageIdStr)) {
				int lastMessageId = Integer.parseInt(lastMessageIdStr);
				String userEmailAddress = (String)request.getSession(true).getAttribute("user");
				User user = channelUserManager.getUser(userEmailAddress);
				user.setLastMessageId(lastMessageId);
				if (lastMessageId == -1) {
					messengerService.snapshot();
				}
			}
		}

	}
	
	private static int messageId = 0;

}
