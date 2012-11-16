package chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chat.service.ChannelAdminManager;

public class ChannelAdminController {

	@Autowired
	private ChannelAdminManager channelAdminManager;
	
//	@RequestMapping("/addChannel")
//	public String addChannel(@RequestParam("channel") String channelId) {
//		//channelAdminManager.addChannel(channelId);
//		
//		return null;
//	}
//	
//	@RequestMapping("/deleteChannel")
//	public String deleteChannel(@RequestParam("channel") String channelId) {
//		//boolean result = channelAdminManager.removeChannel(channelId);
//		return null;
//	}
}
