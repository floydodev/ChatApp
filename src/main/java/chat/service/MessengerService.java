package chat.service;

import chat.model.ChatMessage;

public interface MessengerService extends Runnable  {

	/**
	 * Add message for sending.
	 */
	public void consume(ChatMessage message);
	public void publish();
	public void snapshot();
	public void stop();

}