package chat.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.ContinuationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import chat.model.ChatMessage;
import chat.model.User;
import chat.service.ChannelMessageManager;
 
@Controller
@SessionAttributes({"user"})
public class ChatMessageController {

	static private final Log log = LogFactory.getLog(ChatMessageController.class);
	
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


	private class MessageSender implements Runnable {
		
	    protected boolean running = true;
	    protected final Map<Integer, ChatMessage> messages = new HashMap<Integer, ChatMessage>();
	    private ServletResponse connection;
	    
	    public void send(Map<Integer, ChatMessage> chatMessages) {
	        synchronized (messages) {
	            messages.putAll(chatMessages);
	            log.info("Message added #messages=" + messages.size());
	            messages.notify();
	        }
	    }
		
		public MessageSender(ServletResponse connection) {
			this.connection = connection;
		}
		
		public void run() {
			while (running) {
				// send the messages as soon as message.size() > 0
				while (messages.isEmpty()) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Map<Integer, ChatMessage> pendingMessages = null;
				synchronized (messages) {
					pendingMessages.putAll(messages);
					messages.clear();
				}
				if (connection == null) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				try {
					OutputStream out = connection.getOutputStream();
					// convert messages to JSON and then post to output stream
					// How will this work? Surely it'll be intercepted by Jackson....?
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}