package com.slupicki.lideo.dao;

import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.exceptions.ToLateToCancelException;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.Reservation;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Set<Reservation> findDistinctByCancellationFalseAndPayment_CreatedAtBeforeAndPayment_PaidFalse(ZonedDateTime before);

  Optional<Reservation> findByIdEqualsAndClient_IdEquals(Long reservationId, Long clientId);

  @Modifying
  @Transactional
  default int cancelOverdueReservations(ZonedDateTime before) {
    Set<Reservation> overdueReservations = findDistinctByCancellationFalseAndPayment_CreatedAtBeforeAndPayment_PaidFalse(before);
    overdueReservations.forEach(reservation -> {
      Flight flight = reservation.getFlight();
      flight.setFreeSeats(flight.getFreeSeats() + reservation.getSeats());
      reservation.setCancellation(true);
    });
    saveAll(overdueReservations);
    return overdueReservations.size();
  }

  @Modifying
  @Transactional
  default void cancel(Long id, Long clientId, int howManyDaysBeforeFlightReservationCanBeCanceled) throws NotFoundException, ToLateToCancelException {
    cancel(id, clientId, howManyDaysBeforeFlightReservationCanBeCanceled, ZonedDateTime.now());
  }

  @Modifying
  @Transactional
  default void cancel(Long id, Long clientId, int howManyDaysBeforeFlightReservationCanBeCanceled, ZonedDateTime now)
      throws NotFoundException, ToLateToCancelException {
    Optional<Reservation> reservationOpt = findByIdEqualsAndClient_IdEquals(id, clientId);
    if (!reservationOpt.isPresent()) {
      throw new NotFoundException(MessageFormat.format("Can''t find reservation with id = {0} for client with id = {1}", id, clientId));
    }
    Reservation reservation = reservationOpt.get();
    Flight flight = reservation.getFlight();
    if (now.plusDays(howManyDaysBeforeFlightReservationCanBeCanceled).isAfter(flight.getDepartureTime())) {
      throw new ToLateToCancelException("It is too late to cancel this reservation");
    }
    flight.setFreeSeats(flight.getFreeSeats() + reservation.getSeats());
    reservation.setCancellation(true);
    save(reservation);
  }

}
