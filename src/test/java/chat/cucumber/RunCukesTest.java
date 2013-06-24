package chat.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Cucumber.Options(format = {"json-pretty:target/cucumber-json-report.json"})
public class RunCukesTest {
}