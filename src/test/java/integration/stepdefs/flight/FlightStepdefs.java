package integration.stepdefs.flight;

import static org.assertj.core.api.Assertions.assertThat;

import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.FlightDTO;
import com.slupicki.lideo.testTools.RestTool;
import com.slupicki.lideo.testTools.State;
import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FlightStepdefs implements En {

  private final Logger log = LoggerFactory.getLogger(FlightStepdefs.class);

  @Autowired
  private FlightRepository flightRepository;
  @Autowired
  private RestTool restTool;
  @Autowired
  private State state;

  public FlightStepdefs() {
    When("search by:", (DataTable flightDTODataTable) -> {
      List<FlightDTO> flightDTOs = flightDTODataTable.asList(FlightDTO.class);
      assertThat(flightDTOs).hasSize(1);
      FlightDTO flightDTO = flightDTOs.get(0);
      Set<Flight> flights = flightRepository
          .findDistinctByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThanEqualAndFreeSeatsGreaterThanEqual(
              Optional.ofNullable(flightDTO.getArrival()).orElse(""),
              Optional.ofNullable(flightDTO.getDeparture()).orElse(""),
              Optional.ofNullable(flightDTO.getFromDepartureTime()).orElse(ZonedDateTime.now().minusYears(1000)),
              Optional.ofNullable(flightDTO.getToDepartureTime()).orElse(ZonedDateTime.now().plusYears(1000)),
              Optional.ofNullable(flightDTO.getSeats()).orElse(0)
          );
      state.setFoundFlights(flights);
      log.info("Search found flights: {}", flights.stream().map(Flight::getId).map(Objects::toString).collect(Collectors.joining(",")));
    });

    Then("found flights {word}", (String flightIdsAsString) -> {
      log.info("Looking for {}", flightIdsAsString);
      Set<Long> flightIds = Arrays.stream(flightIdsAsString.split(","))
          .map(StringUtils::deleteWhitespace)
          .map(Long::parseUnsignedLong)
          .collect(Collectors.toSet());
      assertThat(state.getFoundFlights().stream().map(Flight::getId).collect(Collectors.toSet()))
          .containsExactlyInAnyOrderElementsOf(flightIds);
    });
  }
}
