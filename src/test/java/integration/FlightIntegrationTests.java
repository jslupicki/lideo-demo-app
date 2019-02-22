package integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:flights.feature"
    , glue = {"classpath:integration/stepdefs", "classpath:integration/stepdefs/flight"}
)
public class FlightIntegrationTests {

}
