package chat.singlechannel.servlet.service;

import javax.servlet.http.HttpServletRequest;

public interface ClientActionRequestHandler {

	public abstract void handle(HttpServletRequest request, String action);

}