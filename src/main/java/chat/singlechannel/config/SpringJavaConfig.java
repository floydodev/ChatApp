package chat.singlechannel.config;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import chat.multichannel.servlet.service.ClientActionRequestHandler;
import chat.multichannel.servlet.service.ConnectionLifecycleHandler;
import chat.singlechannel.dao.ChatRoom;
import chat.singlechannel.dao.ChatRoomImpl;
import chat.singlechannel.dao.MessagesDAO;
import chat.singlechannel.dao.MessagesDAOInMemoryImpl;
import chat.singlechannel.dao.MessagesDAOJdbcImpl;
import chat.singlechannel.dao.UsersDAO;
import chat.singlechannel.dao.UsersDAOJdbcImpl;
import chat.singlechannel.service.MessageManager;
import chat.singlechannel.service.UserConnectionManager;
import chat.singlechannel.service.UserManager;
import chat.singlechannel.service.messaging.SnapshotMessengerService;
import chat.singlechannel.servlet.service.ClientActionRequestHandlerImpl;
import chat.singlechannel.servlet.service.ConnectionLifecycleHandlerImpl;
import chat.singlechannel.servlet.service.apply.UserConnectionApplyable;

@Configuration
@ImportResource("classpath:applicationContext.xml")
public abstract class SpringJavaConfig {
	
	private final static Log log = LogFactory.getLog(SpringJavaConfig.class);
	
	private @Autowired DataSource dataSource;
	
	@Bean
	public ChatRoom chatRoom() {
		return new ChatRoomImpl(messagesDAO(), usersDAO());
	}

	@Bean
	public MessagesDAO messagesDAO() {
		return new MessagesDAOInMemoryImpl();
	}
//	
//	@Bean
//	public MessagesDAO messagesDAO() {
//		MessagesDAOJdbcImpl messagesDAO = new MessagesDAOJdbcImpl();
//		messagesDAO.setDataSource(dataSource);
//		return messagesDAO;
//	}
	
	@Bean
	public UsersDAO usersDAO() {
		UsersDAOJdbcImpl usersDAO = new UsersDAOJdbcImpl();
		usersDAO.setDataSource(dataSource);
		return usersDAO;
	}
	
	
	@Bean
	public UserManager userManager() {
		return new UserManager(chatRoom());
	}
	
	
	@Bean
	public MessageManager messageManager() {
		return new MessageManager(chatRoom());
	}
	
	@Bean
	public SnapshotMessengerService messengerService() {
		return new SnapshotMessengerService(userConnectionManager(), messageManager());
	}
	
	@Bean
	public UserConnectionManager userConnectionManager() {
		return new UserConnectionManager(userConnectionApplyable());
	}
	
	@Bean
	public UserConnectionApplyable userConnectionApplyable() {
		return new UserConnectionApplyable(messageManager());
	}
	
	@Bean
	public ConnectionLifecycleHandler connectionLifecycleHandler() {
		return new ConnectionLifecycleHandlerImpl(userConnectionManager());
	}
	
	@Bean 
	public ClientActionRequestHandler clientActionRequestHandler() {
		return new ClientActionRequestHandlerImpl(chatRoom(), messengerService());
	}
	
//	@Bean
//	public DataSource dataSource() {
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setDriverClassName(driverClassName)
//		return dataSource;
//		
//	}
	
//	<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
//		<property name="driverClassName" value="com.mysql.jdbc.Driver"/>
//		<property name="url" value="jdbc:mysql://localhost/chatapp"/>
//		<property name="username" value="root"/>
//		<property name="password" value="meanT1meMk"/>
//		<property name="initialSize" value="2"/>
//		<property name="maxActive" value="5"/>
//	</bean>
}
