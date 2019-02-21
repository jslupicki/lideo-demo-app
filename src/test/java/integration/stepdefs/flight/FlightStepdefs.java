package integration.stepdefs.flight;

import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.testTools.RestTool;
import com.slupicki.lideo.testTools.State;
import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;
import java.util.List;
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
    And("load flights to DB:", (DataTable flightsDataTable) -> {
      List<Flight> flights = flightsDataTable.asList(Flight.class);
      flightRepository.saveAll(flights);
      flights.forEach(flight -> log.info("Load flight: {}", flight));
    });
    Given("^test$", () -> {
    });
  }
}
