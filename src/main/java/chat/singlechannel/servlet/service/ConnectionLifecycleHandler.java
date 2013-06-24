package chat.singlechannel.servlet.service;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.comet.CometEvent;

public interface ConnectionLifecycleHandler {

	public abstract void handle(CometEvent event)
			throws UnsupportedOperationException, IOException, ServletException;

}