package com.slupicki.lideo.rest;

import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.FlightDTO;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/flight")
public class FlightController {

  private final FlightRepository flightRepository;

  public FlightController(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  @GetMapping("/departure")
  public Set<String> getDepartures() {
    return flightRepository.departures();
  }

  @GetMapping("/arrival")
  public Set<String> getArrivals() {
    return flightRepository.arrivals();
  }

  @PostMapping("/search")
  public Set<Flight> findFlight(@RequestBody FlightDTO flightDTO) {
    return flightRepository
        .findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThanEqualAndFreeSeatsGreaterThanEqual(
            Optional.ofNullable(flightDTO.getArrival()).orElse(""),
            Optional.ofNullable(flightDTO.getDeparture()).orElse(""),
            Optional.ofNullable(flightDTO.getFromDepartureTime()).orElse(ZonedDateTime.now().minusYears(1000)),
            Optional.ofNullable(flightDTO.getToDepartureTime()).orElse(ZonedDateTime.now().plusYears(1000)),
            Optional.ofNullable(flightDTO.getSeats()).orElse(0)
        );
  }

  @GetMapping("/search/template")
  public FlightDTO searchTemplate() {
    return FlightDTO.builder()
        .departure("departure example")
        .arrival("arrival example")
        .fromDepartureTime(ZonedDateTime.now().minusDays(10))
        .toDepartureTime(ZonedDateTime.now())
        .seats(3)
        .build();
  }
}
