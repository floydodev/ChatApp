package chat.multichannel.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import chat.multichannel.dao.ChannelDAOInMemoryImpl;
import chat.multichannel.model.Channel;
import chat.multichannel.service.ChannelMessageManager;
import chat.multichannel.service.ChannelUserManager;
import chat.multichannel.service.SnapshotMessengerService;
import chat.multichannel.service.UserConnectionManager;

@Configuration
public abstract class SpringJavaConfig {
	
	private final static Log log = LogFactory.getLog(SpringJavaConfig.class);
	
	@Bean
	public Channel defaultChannel() {
		log.info("Awesome 1");
		return new Channel();
	}

	@Bean
	public ChannelDAOInMemoryImpl channelDAO() {
		log.info("Awesome 2");
		return new ChannelDAOInMemoryImpl(defaultChannel());
	}
	
	@Bean
	public ChannelUserManager channelUserManager() {
		return new ChannelUserManager(channelDAO());
	}
	
	
	@Bean
	public ChannelMessageManager channelMessageManager() {
		return new ChannelMessageManager(channelDAO());
	}
	
	@Bean
	public SnapshotMessengerService messengerService() {
		return new SnapshotMessengerService(userConnectionManager());
	}
	
	@Bean
	public UserConnectionManager userConnectionManager() {
		return new UserConnectionManager();
	}
//	@ExternalBean
//	public abstract DataSource dataSource();
}
