package chat.multichannel.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.multichannel.model.ChatMessage;
import chat.multichannel.model.User;

import com.google.gson.Gson;

public class SnapshotMessengerServiceRefactored implements MessengerService {

	private static final Log log = LogFactory.getLog(SnapshotMessengerService.class);
	
	//private NavigableMap<Integer, ChatMessage> messageRepoMap = new TreeMap<Integer, ChatMessage>();
	private Map<Integer, ChatMessage> messagePayloadMap = new HashMap<Integer, ChatMessage>();
	
	private UserConnectionManager userConnectionManager;
	private ChannelMessageManager channelMessageManager;
	private boolean snapshotRequested = false;
	private int snapshotSize = 10;
	private boolean running = true;

	public SnapshotMessengerServiceRefactored(UserConnectionManager userConnectionManager, 
												ChannelMessageManager channelMessageManager) {
		this.userConnectionManager = userConnectionManager;
		this.channelMessageManager = channelMessageManager;
	}

	public SnapshotMessengerServiceRefactored(UserConnectionManager userConnectionManager, 
												ChannelMessageManager channelMessageManager, 
												int snapshotSize) {
		this(userConnectionManager, channelMessageManager);
		this.snapshotSize = snapshotSize;
	}

	public void stop() {
		running = false;
	}

	public void run() {
		while (running) {
			publish();
		}
	}
	
	public void consume(String message, User user) {
		synchronized(channelMessageManager) {
			channelMessageManager.addMessage(message, Calendar.getInstance().getTime(), user);
			//messagePayloadMap.put(message.getMessageId(), message);
			//messagePayloadMap.notify();
			channelMessageManager.notify();
		}

	}
	
	public void snapshot() {
		synchronized(channelMessageManager) {
			snapshotRequested = true;
			channelMessageManager.notify();
		}

	}

	public void publish() {
		while (!channelMessageManager.hasNewMessages() && !snapshotRequested) {
			try {
				synchronized (channelMessageManager) {
					channelMessageManager.wait();	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		synchronized (channelMessageManager) {
			
			// Copy received messages into repo
//			for (Map.Entry<Integer, ChatMessage> entry : messagePayloadMap.entrySet()) {
//				channelMessageManager.addMessage(entry.getValue().getText(), 
//						Calendar.getInstance().getTime(), entry.getValue().getUser());
//			}
			//messagePayloadMap.clear();
			if (channelMessageManager.getMessages().isEmpty()) {
				// Nothing to publish
				return;
			}
			// Now loop through all actice connections and query the repo
			// to get list of unsent messages for each connection
			// (based on lastMessageId)
			// Send any pending messages to any logged in users
			synchronized(userConnectionManager) {
				for (User user : userConnectionManager.getUserConnectionMap().keySet()) {
					try {
						NavigableMap<Integer, ChatMessage> pendingMessages = new TreeMap<Integer, ChatMessage>();
						if (user.getLastMessageId() == -1) {
							// send up to 'snapshotSize' num of messages back to the client
							pendingMessages.putAll(channelMessageManager.getLastXMessages(snapshotSize));
						} else {
							// send only the messages not yet sent during this connection session
							//pendingMessages.putAll(messageRepoMap.tailMap(user.getLastMessageId() + 1));
							pendingMessages.putAll(channelMessageManager.getMessagesSince(user.getLastMessageId()));
						}
						if (!pendingMessages.isEmpty()) {
							// TODO Why did I need to add this? Wouldn't the polling cycle ensure
							// the client always passed up the lastMessageId received? Why explicitly set it?
							// What scenarios would cause a double send?
							user.setLastMessageId(pendingMessages.lastKey());
							PrintWriter writer = userConnectionManager.getConnection(user).getWriter();
							Gson gson = new Gson();
							String jsonString = gson.toJson(pendingMessages);
							writer.println(jsonString);
							writer.flush();
							writer.close();	/* the response will not be sent until the writer is closed */
							for (Map.Entry <Integer, ChatMessage> entry : pendingMessages.entrySet()) {
								log.info("user=" + user.getEmailAddress() + " - Sent: " + entry.getValue());
							}
						}
					} catch (IOException e) {
						log.error("IOExeption sending message", e);
					}
				}
			}
			// update lastMessageId of each user as we send out the messages
			channelMessageManager.newMessagesProcessed();
			snapshotRequested = false;
		}

	}

	public void consume(ChatMessage message) {
		// TODO Auto-generated method stub
		
	}

}
