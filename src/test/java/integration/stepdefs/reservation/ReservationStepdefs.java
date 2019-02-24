package integration.stepdefs.reservation;

import static com.slupicki.lideo.testTools.RestTool.ADD_RESERVATION;
import static com.slupicki.lideo.testTools.RestTool.CANCEL_RESERVATION;
import static com.slupicki.lideo.testTools.RestTool.EMPTY_PARAMS;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import com.slupicki.lideo.model.Payment;
import com.slupicki.lideo.model.Reservation;
import com.slupicki.lideo.model.ReservationDTO;
import com.slupicki.lideo.testTools.RestTool;
import com.slupicki.lideo.testTools.State;
import cucumber.api.java8.En;
import integration.stepdefs.flight.FlightStepdefs;
import io.cucumber.datatable.DataTable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ReservationStepdefs implements En {

  private final Logger log = LoggerFactory.getLogger(FlightStepdefs.class);

  @Autowired
  private RestTool restTool;
  @Autowired
  private State state;

  public ReservationStepdefs() {
    Then("client have to pay {big_decimal} zł", (BigDecimal haveToPay) -> {
      Payment payment = state.getPayment();
      assertThat(payment).isNotNull();
      assertThat(payment.getAmount().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(haveToPay.setScale(2, RoundingMode.HALF_DOWN));
    });

    When("client create reservation:", (DataTable reservationDTODataTable) -> {
      List<ReservationDTO> reservationDTOs = reservationDTODataTable.asList(ReservationDTO.class);
      assertThat(reservationDTOs).hasSize(1);
      ReservationDTO reservationDTO = reservationDTOs.get(0);
      log.info("About to create reservation {}", reservationDTO);
      Reservation reservation = restTool.post(ADD_RESERVATION, Reservation.class, reservationDTO, EMPTY_PARAMS, EMPTY_PARAMS)
          .orElseThrow(RuntimeException::new);
      state.setReservation(reservation);
      state.setPayment(reservation.getPayment());
      log.info("Created reservation {}", reservation);
    });

    When("client cancel reservation {long}", (Long reservationId) -> {
      Optional<String> id = restTool.delete(CANCEL_RESERVATION, String.class, EMPTY_PARAMS, ImmutableMap.of("id", reservationId.toString()));
    });
  }
}
