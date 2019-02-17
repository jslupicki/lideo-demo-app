package integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:registration.feature"
        , glue = {"classpath:integration/stepdefs", "classpath:integration/stepdefs/client"}
)
public class RegistrationIntegrationTests extends SpringIntegrationTest {
}
