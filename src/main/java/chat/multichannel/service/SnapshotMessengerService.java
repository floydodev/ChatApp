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

public class SnapshotMessengerService implements MessengerService {

	private static final Log log = LogFactory.getLog(SnapshotMessengerService.class);
	
	private NavigableMap<Integer, ChatMessage> messageRepoMap = new TreeMap<Integer, ChatMessage>();
	private Map<Integer, ChatMessage> messagePayloadMap = new HashMap<Integer, ChatMessage>();
	
	private UserConnectionManager userConnectionManager = null;
	private boolean snapshotRequested = false;
	private int snapshotSize;
	private boolean running = true;

	public SnapshotMessengerService(UserConnectionManager userConnectionManager) {
		this.userConnectionManager = userConnectionManager;
		this.snapshotSize = 10;
	}
	
	public void stop() {
		running = false;
	}

	public void run() {
		while (running) {
			publish();
		}
	}
	
	public void consume(String messageText, User user) {
		synchronized(messagePayloadMap) {
			ChatMessage message = new ChatMessage(messageText, Calendar.getInstance().getTime(), user, ++messageId);
			messagePayloadMap.put(message.getMessageId(), message);
			messagePayloadMap.notify();
		}
	}
	
	private static int messageId = 0;
	
	public void snapshot() {
		synchronized(messagePayloadMap) {
			snapshotRequested = true;
			messagePayloadMap.notify();
		}

	}

	public void publish() {
		while (messagePayloadMap.isEmpty() && !snapshotRequested) {
			try {
				synchronized (messagePayloadMap) {
					messagePayloadMap.wait();	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		synchronized (messagePayloadMap) {
			snapshotRequested = false;
			// Copy received messages into repo
			messageRepoMap.putAll(messagePayloadMap);
			messagePayloadMap.clear();
			if (messageRepoMap.isEmpty()) {
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
						int latestMessageId = messageRepoMap.lastKey();
						Map<Integer, ChatMessage> pendingMessages = new TreeMap<Integer, ChatMessage>();
						
						if (user.getLastMessageId() == -1) {
							// terse version
								//pendingMessages.putAll(messageRepoMap.tailMap(latestMessageId < 10 ? 0 : latestMessageId - 10));
							// verbose version
							if (latestMessageId < snapshotSize) {
								pendingMessages.putAll(messageRepoMap.tailMap(0));
								// send all messages from the repo (it's 
							} else {
								pendingMessages.putAll(messageRepoMap.tailMap((latestMessageId - snapshotSize) + 1));
								// send a many messages as 'snotshotSize' specifiies
							}
						} else {
							// send only the messages not yet sent during this connection session
							pendingMessages.putAll(messageRepoMap.tailMap(user.getLastMessageId() + 1));
						}
						if (!pendingMessages.isEmpty()) {
							// TODO Why did I need to add this? Wouldn't the polling cycle ensure
							// the client always passed up the lastMessageId received? Why explicitly set it?
							// What scenarios would cause a double send?
							user.setLastMessageId(latestMessageId);
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
			
		}

	}

}
