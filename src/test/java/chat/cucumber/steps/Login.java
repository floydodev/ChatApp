package chat.cucumber.steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

import chat.pageobjects.LoginPage;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Login {

	private LoginPage loginPage;
	
	@Given("^the user has browsed to homepage$")
	public void the_user_has_browsed_to_homepage() throws Throwable {
		WebDriver webDriver = new HtmlUnitDriver();
		webDriver.get("http://localhost:8080/ChatApp.link/html/login.html");
		loginPage = PageFactory.initElements(webDriver, LoginPage.class);
	    // Express the Regexp above with the code you wish you had
	    throw new PendingException();
	}

	@When("^the homepage loads$")
	public void the_homepage_loads() throws Throwable {
	    // Express the Regexp above with the code you wish you had
	    throw new PendingException();
	}

	@Then("^the user sees the login form$")
	public void the_user_sees_the_login_form() throws Throwable {
		loginPage.enterDisplayName("Otto Tester");
		loginPage.enterEmailAddress("anyvalue@emailaddress.com");
	    // Express the Regexp above with the code you wish you had
	    throw new PendingException();
	}
}
