package integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:reservations.feature"
    , glue = {
    "classpath:integration/stepdefs"
    , "classpath:integration/stepdefs/reservation"
    , "classpath:integration/stepdefs/client"
}
)
public class ReservationIntegrationTests {

}
