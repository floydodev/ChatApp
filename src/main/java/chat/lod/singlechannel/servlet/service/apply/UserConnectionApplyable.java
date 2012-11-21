package chat.lod.singlechannel.servlet.service.apply;

import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.lod.singlechannel.service.MessageManager;

public class UserConnectionApplyable implements Applyable<String, PrintWriter>{
	
	private MessageManager messageManager;
	
	private final static Log log = LogFactory.getLog(UserConnectionApplyable.class);
	
	public UserConnectionApplyable(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	
//	public void each(String userEmailAddress, PrintWriter connection) {
//		messagePusher.pushPendingMessages(userEmailAddress, connection);
//	}

	public void each(String userEmailAddress, PrintWriter connection) {
		messageManager.pushPendingMessages(userEmailAddress, connection);
	}
}
