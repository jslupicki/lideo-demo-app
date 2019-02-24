package com.slupicki.lideo.misc;

import com.slupicki.lideo.dao.ReservationRepository;
import java.time.Period;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

  private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

  private final Period howLongBeforeCancelUnpaidReservation;
  private final ReservationRepository reservationRepository;
  private final TimeProvider timeProvider;

  public Scheduler(
      @Value("${scheduler.cancelling_unpaid_reservations.period}")
          Period howLongBeforeCancelUnpaidReservation,
      ReservationRepository reservationRepository,
      TimeProvider timeProvider
  ) {
    this.howLongBeforeCancelUnpaidReservation = howLongBeforeCancelUnpaidReservation;
    this.reservationRepository = reservationRepository;
    this.timeProvider = timeProvider;
  }

  @Scheduled(cron = "${scheduler.cancelling_unpaid_reservations}")
  public void cancellingUnpaidReservations() {
    ZonedDateTime cancelBefore = timeProvider.getTime().minus(howLongBeforeCancelUnpaidReservation);
    log.info("Start cancelling unpaid reservations (all unpaid before {})", cancelBefore);
    int howManyCanceled = reservationRepository.cancelOverdueReservations(cancelBefore);
    log.info("Canceled {} reservations", howManyCanceled);
  }
}
