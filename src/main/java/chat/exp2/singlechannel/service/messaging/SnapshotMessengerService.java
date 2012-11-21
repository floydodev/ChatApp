package chat.exp2.singlechannel.service.messaging;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.exp2.singlechannel.dao.ChatRoomComposition;
import chat.exp2.singlechannel.service.UserConnectionManager;

public class SnapshotMessengerService {
	//implements MessengerService {

	private static final Log log = LogFactory.getLog(SnapshotMessengerService.class);
	
	private UserConnectionManager userConnectionManager;
	//private OldChatRoomIntf messageManager;
	private ChatRoomComposition messageManager;
//	private MessageManager messageManager;
	private boolean snapshotRequested = false;
	private boolean running = true;
//	private Applyable<String, PrintWriter> userConnectionApplyable;

	public SnapshotMessengerService(UserConnectionManager userConnectionManager, ChatRoomComposition messageManager) {
		this.userConnectionManager = userConnectionManager;
		this.messageManager = messageManager;
	}

	public void stop() {
		running = false;
	}

	public void run() {
		while (running) {
			publish();
		}
	}
	
	public void consume(String message, String emailAddress) {
		synchronized(messageManager) {
			boolean added = messageManager.addMessage(message, Calendar.getInstance().getTime(), emailAddress);
			if (added) {
				messageManager.notify();
			} else {
				log.error("Problem adding message - check logs");
			}
		}
	}
	
	public void snapshot() {
		synchronized(messageManager) {
			snapshotRequested = true;
			messageManager.notify();
		}
	}

	public void publish() {
		while (!messageManager.hasNewMessages() && !snapshotRequested) {
			try {
				synchronized (messageManager) {
					messageManager.wait();	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		synchronized (messageManager) {

//			if (!channelMessageManager.hasMessages()) {
//				// Nothing to publish
//				return; // can't reach here
//			}
			// Now loop through all actice connections and query the repo
			// to get list of unsent messages for each connection
			// (based on lastMessageId)
			// Send any pending messages to any logged in users
			synchronized(userConnectionManager) {
				//userConnectionManager.applyConnectionLogic(userConnectionApplyable);
				userConnectionManager.applyConnectionLogic();
			}
			// update lastMessageId of each user as we send out the messages
			messageManager.newMessagesProcessed();
			snapshotRequested = false;
		}

	}
	
}
