package chat.singlechannel.dao;

import java.util.NavigableMap;

import chat.singlechannel.dto.Message;

public interface MessagesDAO {

	public void put(int id, Message message);

	public boolean isEmpty();

	public int lastKey();

	public NavigableMap<Integer, Message> tailMap(int userLastMessageId);

}