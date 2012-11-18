package chat.servlet.service;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.comet.CometEvent;

public interface ConnectionLifecycleHandler {
	
	public void handle(CometEvent event) throws UnsupportedOperationException, IOException, ServletException;

}
