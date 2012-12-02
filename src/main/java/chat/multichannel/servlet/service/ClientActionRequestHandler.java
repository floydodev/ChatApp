package chat.multichannel.servlet.service;

import javax.servlet.http.HttpServletRequest;

public interface ClientActionRequestHandler {
	
	public void handle(HttpServletRequest request, String action);
	
}