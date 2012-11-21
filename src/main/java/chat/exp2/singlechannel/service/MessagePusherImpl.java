package chat.exp2.singlechannel.service;

import java.io.PrintWriter;

import com.google.gson.Gson;

public class MessagePusherImpl {
	
	private MessageManager messageManager;
	private UserManager userManager;

	public void pushPendingMessages(String userEmailAddress, PrintWriter writer) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(messageManager.getMessagesSince(userManager.getUserLastMessageId(userEmailAddress)));
		writer.println(jsonString);
		writer.flush();
		writer.close();
	}
	
}
