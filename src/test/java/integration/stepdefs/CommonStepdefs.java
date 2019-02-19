package integration.stepdefs;

import com.slupicki.lideo.ConfigurableTimeProviderImpl;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.dao.PaymentRepository;
import com.slupicki.lideo.dao.ReservationRepository;
import com.slupicki.lideo.testTools.RestTool;
import cucumber.api.java8.En;
import integration.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonStepdefs extends SpringIntegrationTest implements En {

    @Autowired
    private ConfigurableTimeProviderImpl configurableTimeProvider;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RestTool restTool;

    public CommonStepdefs() {
        Given("empty DB", () -> {
            reservationRepository.deleteAll();
            paymentRepository.deleteAll();
            flightRepository.deleteAll();
            clientRepository.deleteAll();
        });

        Then("status is {word}", (String status) -> {
            assertThat(restTool.statusCode).isEqualTo(HttpStatus.valueOf(status));
        });

    }
}
