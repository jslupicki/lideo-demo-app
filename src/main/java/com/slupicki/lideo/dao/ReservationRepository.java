package com.slupicki.lideo.dao;

import com.slupicki.lideo.exceptions.NotFoundException;
import com.slupicki.lideo.exceptions.ToLateToCancelException;
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
    overdueReservations.forEach(reservation -> reservation.setCancellation(true));
    saveAll(overdueReservations);
    return overdueReservations.size();
  }

  @Modifying
  @Transactional
  default void cancel(Long id, Long clientId, int howManyDaysBeforeFlightReservationCanBeCanceled) throws NotFoundException, ToLateToCancelException {
    Optional<Reservation> reservationOpt = findByIdEqualsAndClient_IdEquals(id, clientId);
    if (!reservationOpt.isPresent()) {
      throw new NotFoundException(MessageFormat.format("Can''t find reservation with id = {0} for client with id = {1}", id, clientId));
    }
    Reservation reservation = reservationOpt.get();
    if (ZonedDateTime.now().plusDays(howManyDaysBeforeFlightReservationCanBeCanceled).isAfter(reservation.getFlight().getDepartureTime())) {
      throw new ToLateToCancelException("It is too late to cancel this reservation");
    }
    reservation.setCancellation(true);
    save(reservation);
  }

}
