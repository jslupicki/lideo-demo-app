package integration.stepdefs;

import com.slupicki.lideo.ConfigurableTimeProviderImpl;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.dao.PaymentRepository;
import com.slupicki.lideo.dao.ReservationRepository;
import cucumber.api.java8.En;
import integration.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

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

    public CommonStepdefs() {
        Given("empty DB", () -> {
            reservationRepository.deleteAll();
            paymentRepository.deleteAll();
            flightRepository.deleteAll();
            clientRepository.deleteAll();
        });

    }
}
