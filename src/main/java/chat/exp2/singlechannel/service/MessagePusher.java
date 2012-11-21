package chat.exp2.singlechannel.service;

import java.io.PrintWriter;
import java.util.Map;

import chat.exp2.singlechannel.dto.Message;

public interface MessagePusher {

	public void pushPendingMessages(String userEmailAddress, PrintWriter writer);
}
