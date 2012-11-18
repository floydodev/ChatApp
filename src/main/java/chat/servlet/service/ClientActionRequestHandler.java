package chat.servlet.service;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import chat.model.ChatMessage;
import chat.model.User;
import chat.util.ChatClientAction;

public interface ClientActionRequestHandler {
	
	public void handle(HttpServletRequest request, String action);
	
}