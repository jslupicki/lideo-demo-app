package com.slupicki.lideo;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.dao.PaymentRepository;
import com.slupicki.lideo.dao.ReservationRepository;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.Reservation;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DaoTests {

  @Autowired
  private ClientRepository clientRepository;
  @Autowired
  private FlightRepository flightRepository;
  @Autowired
  private PaymentRepository paymentRepository;
  @Autowired
  private ReservationRepository reservationRepository;

  @Before
  public void setUp() throws Exception {
    cleanDB();
  }

  @After
  public void tearDown() throws Exception {
    cleanDB();
  }

  @Test
  public void to_play_a_bit_with_repositories() {
    Client client1 = Client.of("client1");
    Flight flight1 = Flight.of("Warsaw", "London", ZonedDateTime.of(2016, 2, 16, 0, 0, 0, 0, ZoneId.of("UTC")), 200, BigDecimal.valueOf(2.3));
    flightRepository.save(flight1);

    Reservation reservation1 = Reservation.of(client1, flight1, 2, BigDecimal.valueOf(4.6));
    client1.setReservations(ImmutableList.of(reservation1));
    clientRepository.saveAndFlush(client1);

    System.out.println("*************************************");
    System.out.println("Client: " + client1);
    System.out.println("*************************************");
  }

  @Test
  public void findByLoginTest() {
    Client l1 = Client.builder().login("l1").build();
    Client l2 = Client.builder().login("l2").build();

    assertThat(clientRepository.countByLogin("l1")).isEqualTo(0);

    clientRepository.save(l1);
    clientRepository.save(l2);

    assertThat(clientRepository.countByLogin("l1")).isEqualTo(1);
    assertThat(clientRepository.countByLogin("l2")).isEqualTo(1);
  }

  @Test
  public void flightDeparturesAndArrivalsTest() {
    Flight flightA = Flight.builder().departure("A>").arrival(">A").build();
    Flight flightB = Flight.builder().departure("B>").arrival(">B").build();
    Flight flightC = Flight.builder().departure("C>").arrival(">C").build();
    Flight flightC2 = Flight.builder().departure("C>").arrival(">C").build();

    flightRepository.saveAll(ImmutableList.of(
        flightA,
        flightB,
        flightC,
        flightC2
    ));

    Set<String> departures = flightRepository.departures();
    assertThat(departures).containsExactlyInAnyOrder("A>", "B>", "C>");
    Set<String> arrivals = flightRepository.arrivals();
    assertThat(arrivals).containsExactlyInAnyOrder(">A", ">B", ">C");
  }

  @Test
  public void findFlight() {
    Flight flightA = Flight.builder().departure("Wroclaw").arrival("Warsaw").departureTime(ZonedDateTime.now().plusDays(3)).freeSeats(10).build();
    Flight flightB = Flight.builder().departure("Bydgoszcz").arrival("Warsaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10).build();
    Flight flightC = Flight.builder().departure("Gdansk").arrival("Wroclaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10).build();
    Flight flightD = Flight.builder().departure("Warsaw").arrival("Wroclaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10).build();

    flightRepository.saveAll(ImmutableList.of(
        flightA,
        flightB,
        flightC,
        flightD
    ));

    Set<Flight> flights = flightRepository
        .findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDepartureTimeGreaterThanEqualAndDepartureTimeLessThanEqualAndFreeSeatsGreaterThanEqual(
            "warsaw",
            "wroclaw",
            ZonedDateTime.now().minusDays(20),
            ZonedDateTime.now().plusDays(20),
            0
        );
    System.out.println(flights);
  }

  private void cleanDB() {
    reservationRepository.deleteAll();
    paymentRepository.deleteAll();
    flightRepository.deleteAll();
    clientRepository.deleteAll();
  }
}
