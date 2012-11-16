package chat.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.dao.ChannelDAO;
import chat.dao.ChannelDAOInMemoryImpl;
import chat.model.Channel;
import chat.model.User;
import chat.service.ChannelUserManager;

public class LoginControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void login_AddsUser_ReturnsView() {
		Channel channel = new Channel();
		ChannelDAO channelDAO = new ChannelDAOInMemoryImpl(channel);
		ChannelUserManager channelUserMgr = new ChannelUserManager(channelDAO);
		LoginController loginController = new LoginController();
		loginController.setChannelUserManager(channelUserMgr);
		String userDisplayName = "Mr Test";
		String userEmailAddress = "test@test.com";
		User user = loginController.createUser();
		String expectedView = "chatHome";
		String actualView = loginController.login(userDisplayName, userEmailAddress, user);
		
		assertEquals(expectedView, actualView);
		boolean found = false;
		for (User addedUser : channelUserMgr.getUsers()) {
			if (addedUser.getDisplayName().equals(userDisplayName)
					&& addedUser.getEmailAddress().equals(userEmailAddress)) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

}
