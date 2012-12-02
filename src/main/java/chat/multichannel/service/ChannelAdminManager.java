package chat.multichannel.service;

import chat.multichannel.dao.ChannelDAO;
import chat.multichannel.model.Channel;

public class ChannelAdminManager {
	
	private ChannelDAO channelDAO;
	
	public ChannelAdminManager(ChannelDAO channelDAO) {
		this.channelDAO = channelDAO;
	}
	
//	public Channel getChannel(String channelId) {
//		return channelDAO.getChannel(channelId);
//	}
	
	public void addChannel(String channelId) {
		channelDAO.addChannel(new Channel(channelId));
	}

	public boolean removeChannel(String channelId) {
		return channelDAO.removeChannel(channelId);
	}
	
}
