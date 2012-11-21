package chat.lod.singlechannel.service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import chat.lod.singlechannel.dto.Message;
import chat.lod.singlechannel.servlet.service.apply.Applyable;

public class UserConnectionManager {

	//private final static Log log = LogFactory.getLog(UserConnectionManager.class);
	
	private Map<String, PrintWriter> userConnectionMap;
	private Applyable<String, PrintWriter> userConnectionApplyable;
	
	public UserConnectionManager(Applyable<String, PrintWriter> userConnectionApplyable) {
		this.userConnectionMap = new HashMap<String, PrintWriter>();
		this.userConnectionApplyable = userConnectionApplyable;
	}

	public boolean removeUserConnection(String userEmailAddress) {
		return userConnectionMap.remove(userEmailAddress) != null;
	}

	public boolean addUserConnection(String userEmailAddress, PrintWriter connection) {
		return userConnectionMap.put(userEmailAddress, connection) != null;
	}

	public void applyConnectionLogic() {
		for (Map.Entry<String, PrintWriter> entry : userConnectionMap.entrySet()) {
			userConnectionApplyable.each(entry.getKey(), entry.getValue());
		}
	}
	
}
