package chat.lod.singlechannel.service;

import java.io.PrintWriter;
import java.util.Map;

import chat.lod.singlechannel.dto.Message;

import com.google.gson.Gson;

public class MessagePusherImpl {

	public void pushPendingMessages(String userEmailAddress, PrintWriter writer, Map<Integer, Message> pendingMessages) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(pendingMessages);
		writer.println(jsonString);
		writer.flush();
		writer.close();
	}
	
}
