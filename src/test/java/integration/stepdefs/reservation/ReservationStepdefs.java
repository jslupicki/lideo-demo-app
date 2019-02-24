package integration.stepdefs.reservation;

import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.testTools.RestTool;
import com.slupicki.lideo.testTools.State;
import cucumber.api.java8.En;
import integration.stepdefs.flight.FlightStepdefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ReservationStepdefs implements En {

  private final Logger log = LoggerFactory.getLogger(FlightStepdefs.class);

  @Autowired
  private FlightRepository flightRepository;
  @Autowired
  private RestTool restTool;
  @Autowired
  private State state;

  public ReservationStepdefs() {
  }
}
