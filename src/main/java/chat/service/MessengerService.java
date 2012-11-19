package chat.service;

import chat.model.User;

public interface MessengerService extends Runnable  {

	/**
	 * Add message for sending.
	 */
	public void consume(String message, User user);
	public void publish();
	public void snapshot();
	public void stop();

}