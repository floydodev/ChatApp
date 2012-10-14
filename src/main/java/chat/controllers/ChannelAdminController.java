package chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import chat.manager.ChannelAdminManager;

public class ChannelAdminController {

	@Autowired
	private ChannelAdminManager channelAdminManager;
	
	@RequestMapping("/addChannel")
	public String addChannel() {
		// TODO
		return null;
	}
	
	@RequestMapping("/deleteChannel")
	public String deleteChannel() {
		// TODO
		return null;
	}
}
