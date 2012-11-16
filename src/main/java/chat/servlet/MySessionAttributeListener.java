package chat.servlet;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MySessionAttributeListener implements HttpSessionAttributeListener {
	
	private final static Log log = LogFactory.getLog(MySessionAttributeListener.class);

	public void attributeAdded(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		log.info("jjj Attribute added : " + attributeName + " : " + attributeValue);
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		log.info("xxx Attribute removed : " + attributeName + " : " + attributeValue);
	}
 
	public void attributeReplaced(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		log.info("zzz Attribute replaced : " + attributeName + " : " + attributeValue);	
	}

	public void dummyMethod1(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		System.out.println("yyy Attribute replaced : " + attributeName + " : " + attributeValue);	
	}

	public void dummyMethod2(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		System.out.println("yyy Attribute replaced : " + attributeName + " : " + attributeValue);	
	}
	
	public void dummyMethod3(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		System.out.println("yyy Attribute replaced : " + attributeName + " : " + attributeValue);	
	}
 
}
