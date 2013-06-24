package chat.pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
	
	@FindBy(using="user_display_name")
	private WebElement userName;
	
	@FindBy(using="user_email_address")
	private WebElement emailAddress;
	
	@FindBy(using="btn-submit")
	private WebElement loginButton;

	public void enterDisplayName(String string) {
		// TODO Auto-generated method stub
		
	}

	public void enterEmailAddress(String string) {
		// TODO Auto-generated method stub
		
	}
	

}
