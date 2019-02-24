package com.slupicki.lideo.rest;

import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.dao.ReservationRepository;
import com.slupicki.lideo.exceptions.NotEnoughtSeatsInFlightException;
import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.exceptions.NotLoggedInException;
import com.slupicki.lideo.exceptions.ToLateToCancelException;
import com.slupicki.lideo.misc.PriceCalculator;
import com.slupicki.lideo.misc.TimeProvider;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.Payment;
import com.slupicki.lideo.model.Reservation;
import com.slupicki.lideo.model.ReservationDTO;
import java.math.BigDecimal;
import java.text.MessageFormat;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/reservation")
public class ReservationController {

  private final ReservationRepository reservationRepository;
  private final FlightRepository flightRepository;
  private final ClientRepository clientRepository;
  private final PriceCalculator priceCalculator;
  private final TimeProvider timeProvider;
  private final int howManyDaysBeforeFlightReservationCanBeCanceled;

  public ReservationController(
      ReservationRepository reservationRepository,
      FlightRepository flightRepository,
      ClientRepository clientRepository,
      PriceCalculator priceCalculator,
      TimeProvider timeProvider,
      @Value("${reservation.how.many.days.before.can.be.cancelled}")
          int howManyDaysBeforeFlightReservationCanBeCanceled

  ) {
    this.reservationRepository = reservationRepository;
    this.flightRepository = flightRepository;
    this.clientRepository = clientRepository;
    this.priceCalculator = priceCalculator;
    this.timeProvider = timeProvider;
    this.howManyDaysBeforeFlightReservationCanBeCanceled = howManyDaysBeforeFlightReservationCanBeCanceled;
  }

  @PostMapping("")
  @Transactional
  public Reservation addReservation(@RequestBody ReservationDTO reservationDTO) throws NotFoundException, NotEnoughtSeatsInFlightException {
    Assert.notNull(reservationDTO.getClientId(), "Client ID have to be set");
    Assert.notNull(reservationDTO.getFlightId(), "Flight ID have to be set");
    Assert.notNull(reservationDTO.getSeats(), "Seats number have to be set");
    Assert.isTrue(reservationDTO.getSeats() > 0, "Seats number have to be >0");
    Client client = clientRepository.findById(reservationDTO.getClientId())
        .orElseThrow(() -> new NotFoundException("Can't find client with ID = " + reservationDTO.getClientId()));
    Flight flight = flightRepository.findById(reservationDTO.getFlightId())
        .orElseThrow(() -> new NotFoundException("Can't find flight with ID = " + reservationDTO.getFlightId()));
    if (flight.getFreeSeats() < reservationDTO.getSeats()) {
      throw new NotEnoughtSeatsInFlightException(MessageFormat.format(
          "Flight with ID = {0} have only {1} free seat which is less then requested {2}",
          reservationDTO.getSeats(),
          flight.getFreeSeats(),
          reservationDTO.getSeats()
      ));
    }
    flight.setFreeSeats(flight.getFreeSeats() - reservationDTO.getSeats());
    flightRepository.save(flight);
    BigDecimal price = priceCalculator.calculateReservationPrice(
        flight.getPricePerSeat(),
        reservationDTO.getSeats(),
        client.getLogin() != null,
        reservationDTO.getFasterCheckIn()
    );
    Payment payment = Payment.of(price);
    Reservation reservation = Reservation.of(client, flight, reservationDTO.getSeats(), price, false, payment);
    reservationRepository.save(reservation);
    return reservation;
  }

  @DeleteMapping("/{id}")
  public void cancelReservation(@PathVariable("id") Long id, HttpSession session) throws NotFoundException, NotLoggedInException, ToLateToCancelException {
    Long clientId = (Long) session.getAttribute(ClientController.CLIENT_ID);
    if (clientId == null) {
      throw new NotLoggedInException("You have to log in to cancel reservation");
    }
    reservationRepository.cancel(id, clientId, howManyDaysBeforeFlightReservationCanBeCanceled, timeProvider.getTime());
  }

  @GetMapping("/template")
  public ReservationDTO getTemplate() {
    return ReservationDTO.builder()
        .clientId(1L)
        .flightId(2L)
        .seats(3)
        .fasterCheckIn(false)
        .build();
  }

}
