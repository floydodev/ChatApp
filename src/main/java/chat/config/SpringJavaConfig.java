package chat.config;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import chat.dao.ChannelDAOInMemoryImpl;
import chat.model.Channel;
import chat.service.ChannelMessageManager;
import chat.service.ChannelUserManager;
import chat.service.SnapshotMessengerService;

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
		return new SnapshotMessengerService(channelUserManager());
	}
	
//	@ExternalBean
//	public abstract DataSource dataSource();
}
