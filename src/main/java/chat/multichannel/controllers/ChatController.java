package chat.multichannel.controllers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import chat.multichannel.model.ChatMessage;
import chat.multichannel.model.User;
import chat.multichannel.service.ChannelMessageManager;
 
@Controller
@SessionAttributes({"user"})
public class ChatController {

	static private final Log log = LogFactory.getLog(ChatController.class);
	
	@Autowired
	private ChannelMessageManager channelMessageManager;
	
	/*
	 *  Process the received message
	 */
	
	@RequestMapping(value="/send", method=RequestMethod.POST)
	public String processReceivedMessage(@RequestParam(value="chatMessage", required=true) String message,
											@ModelAttribute("user") User user) {
		log.info("New: Received message=" + message);
		channelMessageManager.addMessage(message, Calendar.getInstance().getTime(), user);
		return "chatHome";
	}

	@RequestMapping(value="/poll", method=RequestMethod.POST)
	public @ResponseBody Map<Integer, ChatMessage> pollForMessages
				(@RequestParam(value="lastMessageId", required=true) int lastMessageId, @ModelAttribute("user") User user) {
		
		log.info("New: Received lastMessageId=" + lastMessageId);
		Map<Integer, ChatMessage> messageMap 
				= lastMessageId == -1 ? processFirstTimePoll(user) : processRegularPoll(user, lastMessageId);
		return messageMap;
	}
	

	private Map<Integer, ChatMessage>  processRegularPoll(User user, int lastMessageId) {
		Map<Integer, ChatMessage> messageMap = new HashMap<Integer, ChatMessage>();
		// TODO what about zero-range check?
		int numMessagesPending = channelMessageManager.getChannel().getLastMessageId() - lastMessageId; 
		if ( numMessagesPending > 0 ) {
			log.info("(" + user.getDisplayName() + " ) Polling for new messages. " + numMessagesPending + " messages pending");
			messageMap = channelMessageManager.getMessagesSince(lastMessageId);
		} else {
			log.info("(" + user.getDisplayName() + " ) Polling for new messages. No new messages");
		}
		return messageMap;
	}
	
	/*
	 * first time poll, send back last 10 messages from this channel
	 */
	private Map<Integer, ChatMessage> processFirstTimePoll(User user) {
		
		log.info("(" + (user == null ? "Unknown" : user.getDisplayName()) + " ) First time poll or perhaps a page reset. So send back last 10 messages");
		return channelMessageManager.getLastXMessages(10);
	}

	public void setChannelManager(ChannelMessageManager channelMessageManager) {
		this.channelMessageManager = channelMessageManager;
	}


}