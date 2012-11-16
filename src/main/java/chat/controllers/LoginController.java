package chat.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import chat.model.User;
import chat.service.ChannelUserManager;

@Controller
@SessionAttributes({"user"})
public class LoginController {
	
	static private final Log log = LogFactory.getLog(LoginController.class);
	
	@Autowired
	private ChannelUserManager channelUserManager;
	
    @ModelAttribute("user")
    public User createUser() {
    	return new User();
    }
	
	@RequestMapping(value="/loginServlet", method=RequestMethod.POST)
	public String login(@RequestParam(value="userDisplayName", required=true) String userDisplayName,
	           @RequestParam(value="userEmailAddress", required=true) String userEmailAddress,
	           @ModelAttribute("user") User user) {
		
		user.setDisplayName(userDisplayName);
		user.setEmailAddress(userEmailAddress);
		channelUserManager.addUser(user);
		log.info("Added user to default Channel: " + user);
		return "chatHome";
	}

	public void setChannelUserManager(ChannelUserManager channelUserManager) {
		this.channelUserManager = channelUserManager;
	}

}
