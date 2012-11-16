package chat.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import chat.model.ChatMessage;
import chat.model.User;

import com.google.gson.Gson;

public abstract class MessengerService implements Runnable  {

	private boolean running = true;
	
	public MessengerService() {
		super();
	}

	public void stop() {
		running = false;
	}

	/**
	 * Add message for sending.
	 */
	public abstract void receive(ChatMessage message);

	public void run() {
		while (running) {
			publish();
		}
	}
	
	public abstract void publish();

}