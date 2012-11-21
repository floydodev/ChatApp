package chat.exp2.singlechannel.servlet.service.apply;

import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.exp2.singlechannel.dto.Message;
import chat.exp2.singlechannel.service.MessagePusher;

public class UserConnectionApplyable implements Applyable<String, PrintWriter>{
	
	private MessagePusher messagePusher;
	
	private final static Log log = LogFactory.getLog(UserConnectionApplyable.class);
	
	public UserConnectionApplyable(MessagePusher messagePusher) {
		this.messagePusher = messagePusher;
	}
	
//	public void each(String userEmailAddress, PrintWriter connection) {
//		messagePusher.pushPendingMessages(userEmailAddress, connection);
//	}

	public void each(String userEmailAddress, PrintWriter connection) {
		messagePusher.pushPendingMessages(userEmailAddress, connection);
	}
}
