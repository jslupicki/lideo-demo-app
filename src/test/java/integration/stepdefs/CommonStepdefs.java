package integration.stepdefs;

import static org.assertj.core.api.Assertions.assertThat;

import com.slupicki.lideo.ConfigurableTimeProviderImpl;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.dao.PaymentRepository;
import com.slupicki.lideo.dao.ReservationRepository;
import com.slupicki.lideo.testTools.RestTool;
import com.slupicki.lideo.testTools.State;
import cucumber.api.java8.En;
import integration.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

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
  @Autowired
  private State state;

  public CommonStepdefs() {
    Given("empty DB", () -> {
      reservationRepository.deleteAll();
      paymentRepository.deleteAll();
      flightRepository.deleteAll();
      clientRepository.deleteAll();
    });

    Given("clear state", () -> {
      state.clear();
    });

    Then("status is {word}", (String status) -> {
      assertThat(state.getHttpStatus()).isEqualTo(HttpStatus.valueOf(status));
    });

  }
}
