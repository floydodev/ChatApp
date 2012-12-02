package chat.singlechannel.service.messaging;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.singlechannel.service.MessageManager;
import chat.singlechannel.service.UserConnectionManager;

public class SnapshotMessengerService implements Runnable {
	//implements MessengerService {

	private static final Log log = LogFactory.getLog(SnapshotMessengerService.class);
	
	private UserConnectionManager userConnectionManager;
	private MessageManager messageManager;
	private boolean running = true;
//	private Applyable<String, PrintWriter> userConnectionApplyable;

	public SnapshotMessengerService(UserConnectionManager userConnectionManager, MessageManager messageManager) {
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
			messageManager.addMessage(message, Calendar.getInstance().getTime(), emailAddress);
			messageManager.notify();
		}
	}
	
	public void snapshot() {
		synchronized(messageManager) {
			messageManager.snapshotRequest();
			messageManager.notify();
		}
	}

	public void publish() {
		while (!messageManager.hasNewMessages() && !messageManager.snapshotRequested()) {
			try {
				synchronized (messageManager) {
					log.info("Wait for someone to notify messageManager");
					messageManager.wait();
					log.info("Someone notified messageManager. I'm expecting there should be new messages or someone has requested a snapshot");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
		}
		synchronized (messageManager) {
			//log.info("Handle snapshot or arrival of new message");
			if (messageManager.hasMessages()) {
				// Now loop through all active connections and query the repo to get list of unsent messages 
				// for each connection. Send any pending messages to any logged in users
				userConnectionManager.applyConnectionLogic();
			}
			
			// Reset the flag states that brought us into this synchronized code block so we can go back to a waiting state at the top of the run loop
			if (messageManager.snapshotRequested()) {
				messageManager.snapshotRequestComplete();
			}
			if (messageManager.hasNewMessages()) {
				messageManager.newMessagesProcessed();
			}

		}
	}

}
