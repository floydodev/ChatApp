package chat.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.model.ChatMessage;
import chat.model.User;

import com.google.gson.Gson;

public class SnapshotMessengerService extends MessengerService {

	private static final Log log = LogFactory.getLog(SnapshotMessengerService.class);
	
	private NavigableMap<Integer, ChatMessage> messageRepoMap = new TreeMap<Integer, ChatMessage>();
	private Map<Integer, ChatMessage> messagePayloadMap = new HashMap<Integer, ChatMessage>();
	
	private ChannelUserManager channelUserManager = null;
	private boolean snapshotRequested = false;
	private int snapshotSize;

	public SnapshotMessengerService(ChannelUserManager channelUserManager) {
		this.channelUserManager  = channelUserManager;
		this.snapshotSize = 10;
	}
	
	@Override
	public void receive(ChatMessage message) {
		synchronized(messagePayloadMap) {
			messagePayloadMap.put(message.getMessageId(), message);
			messagePayloadMap.notify();
		}

	}
	
	public void snapshot() {
		synchronized(messagePayloadMap) {
			snapshotRequested = true;
			messagePayloadMap.notify();
		}

	}

	@Override
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
			for (User user : channelUserManager.getUserConnectionMap().keySet()) {
				try {
					int latestMessageId = messageRepoMap.lastKey();
					Map<Integer, ChatMessage> pendingMessages = new HashMap<Integer, ChatMessage>();
					
					if (user.getLastMessageId() == -1) {
						// terse version
							//pendingMessages.putAll(messageRepoMap.tailMap(latestMessageId < 10 ? 0 : latestMessageId - 10));
						// verbose version
						if (latestMessageId < snapshotSize) {
							pendingMessages.putAll(messageRepoMap.tailMap(0));
							// send all messages from the repo (it's 
						} else {
							pendingMessages.putAll(messageRepoMap.tailMap((latestMessageId - snapshotSize) + 1));
						}
					} else {
						// send only the messages not yet sent during this connection session
						pendingMessages.putAll(messageRepoMap.tailMap(user.getLastMessageId() + 1));
					}
					if (!pendingMessages.isEmpty()) {
						user.setLastMessageId(latestMessageId);
						PrintWriter writer = channelUserManager.getUserConnectionMap().get(user).getWriter();
						Gson gson = new Gson();
						String jsonString = gson.toJson(pendingMessages); 
						log.info("Sending json message: " + jsonString + " to user:" + user.getEmailAddress());
						writer.println(jsonString);
						writer.flush();
						writer.close();	/* the response will not be sent until the writer is closed */
					}
				} catch (IOException e) {
					log.error("IOExeption sending message", e);
				}
			}
			// update lastMessageId of each user as we send out the messages
			
			
		}

	}

}
