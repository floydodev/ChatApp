package chat.singlechannel.dao;

import chat.singlechannel.dto.User;

public interface UsersDAO {

	public abstract boolean containsKey(String userEmailAddress);

	public abstract User get(String userEmailAddress);

	public abstract void put(String emailAddress, User user);

	public abstract int getLastMessageId(String userEmailAddress);

	public abstract void setLastMessageId(String userEmailAddress,
			int lastMessageId);

}