package integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:user.feature"
    , glue = {"classpath:integration/stepdefs", "classpath:integration/stepdefs/client"}
)
public class UserIntegrationTests extends SpringIntegrationTest {

}
