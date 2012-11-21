package chat.exp2.singlechannel.service;

import java.util.Date;
import java.util.Map;

import chat.exp2.singlechannel.dto.Message;

public interface MessageManager {

	public boolean addMessage(String message, Date date, String userEmailAddress);

	public boolean hasNewMessages();

	public boolean hasMessages();

	public void newMessagesProcessed();
	
	public Map<Integer, Message> getMessagesSince(int messageId);
	//	
	//	public void applyConnectionLogic() {
	//		for (Map.Entry<String, PrintWriter> entry : userConnectionMap.entrySet()) {
	//			pushPendingMessages(entry.getKey(), entry.getValue());
	//		}
	//	}
	//	
	//	public void pushPendingMessages(String userEmailAddress, PrintWriter writer) {
	//		Gson gson = new Gson();
	//		String jsonString = gson.toJson(chatRoomDAO.getPendingMessages(userEmailAddress));
	//		writer.println(jsonString);
	//		writer.flush();
	//		writer.close();
	//	}

}