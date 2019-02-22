package com.slupicki.lideo.dao;

import com.slupicki.lideo.model.Flight;
import java.time.ZonedDateTime;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlightRepository extends JpaRepository<Flight, Long> {

  @Query("select distinct departure from Flight")
  Set<String> departures();

  @Query("select distinct arrival from Flight")
  Set<String> arrivals();

  // Probably I shuld rewrite this to @Query("...") but it looks so cool :-)
  Set<Flight> findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThanEqualAndFreeSeatsGreaterThanEqual(
      String arrival,
      String departure,
      ZonedDateTime from,
      ZonedDateTime to,
      Integer seats
  );
}
