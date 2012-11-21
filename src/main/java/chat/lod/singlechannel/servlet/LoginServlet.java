package chat.lod.singlechannel.servlet;

//import static chat.beanFactory.BeanFactory.getInstance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import chat.lod.singlechannel.dao.ChatRoomDAO;

public class LoginServlet extends HttpServlet {
	
	static private final Log log = LogFactory.getLog(LoginServlet.class);
	private ChatRoomDAO userManager = null;
	
	@Override
	public void init() {
		try {
			// There is a Spring-support way of doing DI into Servlets but it requires you to implement
			// HttpRquestHandler or some interface. I should try it but I don't think it will work with the 
			// CometProcessor interface needed by Tomcat
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			userManager = context.getBean("userManager", ChatRoomDAO.class);
		} catch (Exception e) {
			log.error(e);
		}
	}
		
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
													throws IOException, ServletException {

		String userEmailAddress = request.getParameter("userEmailAddress");
		String userDisplayName = request.getParameter("userDisplayName");
		
		userManager.addUser(userEmailAddress, userDisplayName);
		log.info("Added user email to session: " + userEmailAddress);
		request.getSession(true).setAttribute("user", userEmailAddress);
		getServletContext().getRequestDispatcher("/html/chatHome.html").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
													throws IOException, ServletException {
		doGet(request, response);
	}

}
