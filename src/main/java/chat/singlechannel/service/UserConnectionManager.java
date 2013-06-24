package chat.singlechannel.service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chat.singlechannel.servlet.service.apply.Applyable;

public class UserConnectionManager {

	private final static Log log = LogFactory.getLog(UserConnectionManager.class);
	
	private Map<String, PrintWriter> userConnectionMap;
	private Applyable<String, PrintWriter> userConnectionApplyable;
	
	public UserConnectionManager(Applyable<String, PrintWriter> userConnectionApplyable) {
		this.userConnectionMap = new HashMap<String, PrintWriter>();
		this.userConnectionApplyable = userConnectionApplyable;
	}

	public synchronized void removeUserConnection(String userEmailAddress) {
		userConnectionMap.remove(userEmailAddress);
	}

	public synchronized void addUserConnection(String userEmailAddress, PrintWriter connection) {
		userConnectionMap.put(userEmailAddress, connection);
	}

	public synchronized void applyConnectionLogic() {
		log.info("Start - Run through all connections and pass to userConnectionApplyable");
		for (Map.Entry<String, PrintWriter> entry : userConnectionMap.entrySet()) {
			userConnectionApplyable.each(entry.getKey(), entry.getValue());
		}
		log.info("Finish"); 
	}
	
}
