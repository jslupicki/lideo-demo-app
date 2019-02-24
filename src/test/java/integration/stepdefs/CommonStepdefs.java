package integration.stepdefs;

import static org.assertj.core.api.Assertions.assertThat;

import com.slupicki.lideo.ConfigurableTimeProviderImpl;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.dao.PaymentRepository;
import com.slupicki.lideo.dao.ReservationRepository;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.Payment;
import com.slupicki.lideo.model.Reservation;
import com.slupicki.lideo.testTools.RestTool;
import com.slupicki.lideo.testTools.State;
import com.slupicki.lideo.testTools.TimeParser;
import cucumber.api.java8.En;
import integration.SpringIntegrationTest;
import io.cucumber.datatable.DataTable;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class CommonStepdefs extends SpringIntegrationTest implements En {

  private final Logger log = LoggerFactory.getLogger(CommonStepdefs.class);

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

    Given("clear state", () -> state.clear());

    Then("status is {word}", (String status) -> {
      assertThat(state.getHttpStatus()).isEqualTo(HttpStatus.valueOf(status));
    });

    Then("response body contains {string}", (String contains) -> {
      assertThat(state.getResponseBody()).containsIgnoringCase(contains);
    });

    Then("sleep for {int}s", (Integer seconds) -> {
      log.info("Sleep for {}s", seconds);
      Thread.sleep(seconds * 1000L);
    });

    And("load flights to DB:", (DataTable flightsDataTable) -> {
      List<Flight> flights = flightsDataTable.asList(Flight.class);
      flightRepository.saveAll(flights);
      flights.forEach(flight -> log.info("Load flight: {}", flight));
    });

    And("load clients to DB:", (DataTable clientsDataTable) -> {
      List<Client> clients = clientsDataTable.asList(Client.class);
      clientRepository.saveAll(clients);
      clients.forEach(client -> log.info("Load client: {}", client));
    });

    And("load payments to DB:", (DataTable paymentsDataTable) -> {
      List<Payment> payments = paymentsDataTable.asList(Payment.class);
      paymentRepository.saveAll(payments);
      payments.forEach(payment -> log.info("Load payment: {}", payment));
    });

    And("load reservations to DB:", (DataTable reservationsDataTable) -> {
      List<Reservation> reservations = reservationsDataTable.asList(Reservation.class);
      reservations.forEach(reservation -> {
        Flight flight = flightRepository.findById(reservation.getFlight().getId()).orElseThrow(RuntimeException::new);
        Client client = clientRepository.findById(reservation.getClient().getId()).orElseThrow(RuntimeException::new);
        Payment payment = paymentRepository.findById(reservation.getPayment().getId()).orElseThrow(RuntimeException::new);
        reservation.setFlight(flight);
        reservation.setClient(client);
        reservation.setPayment(payment);
      });
      reservationRepository.saveAll(reservations);
      reservations.forEach(reservation -> log.info("Load reservation: {}", reservation));
    });

    Given("set time to {string}", (String newTimeStr) -> {
      ZonedDateTime newTime = TimeParser.parseZonedDateTime(newTimeStr);
      configurableTimeProvider.setTime(newTime);
      log.info("Set current time to {}", newTime);
    });
  }
}
