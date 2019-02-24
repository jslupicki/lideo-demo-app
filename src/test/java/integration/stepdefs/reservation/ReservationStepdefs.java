package integration.stepdefs.reservation;

import static com.slupicki.lideo.testTools.RestTool.ADD_RESERVATION;
import static com.slupicki.lideo.testTools.RestTool.CANCEL_RESERVATION;
import static com.slupicki.lideo.testTools.RestTool.EMPTY_PARAMS;
import static com.slupicki.lideo.testTools.RestTool.PAY_PAYMENT;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import com.slupicki.lideo.dao.ReservationRepository;
import com.slupicki.lideo.model.Payment;
import com.slupicki.lideo.model.PaymentDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ReservationStepdefs implements En {

  private final Logger log = LoggerFactory.getLogger(FlightStepdefs.class);

  @Autowired
  private ReservationRepository reservationRepository;
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
      restTool.delete(CANCEL_RESERVATION, String.class, EMPTY_PARAMS, ImmutableMap.of("id", reservationId.toString()));
    });

    Then("reservation {long} is canceled", (Long reservationId) -> {
      Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(RuntimeException::new);
      assertThat(reservation.getCancellation()).isTrue();
    });

    And("reservation {long} is NOT canceled", (Long reservationId) -> {
      Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(RuntimeException::new);
      assertThat(reservation.getCancellation()).isFalse();
    });

    When("client pay payment {long}", (Long paymentId) -> {
      PaymentDTO paymentDTO = PaymentDTO.builder().paymentId(paymentId).externalId("some external id").build();
      restTool.put(PAY_PAYMENT, String.class, paymentDTO, EMPTY_PARAMS, EMPTY_PARAMS);
    });
  }
}
