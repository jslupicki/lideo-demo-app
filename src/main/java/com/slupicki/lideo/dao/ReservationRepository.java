package com.slupicki.lideo.dao;

import com.slupicki.lideo.model.Reservation;
import java.time.ZonedDateTime;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Set<Reservation> findDistinctByCancellationFalseAndPayment_CreatedAtBeforeAndPayment_PaidFalse(ZonedDateTime before);

  @Modifying
  @Transactional
  default int cancelOverdueReservations(ZonedDateTime before) {
    Set<Reservation> overdueReservations = findDistinctByCancellationFalseAndPayment_CreatedAtBeforeAndPayment_PaidFalse(before);
    overdueReservations.forEach(reservation -> reservation.setCancellation(true));
    saveAll(overdueReservations);
    return overdueReservations.size();
  }
}
