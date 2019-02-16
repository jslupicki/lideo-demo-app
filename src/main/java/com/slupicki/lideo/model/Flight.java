package com.slupicki.lideo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
public class Flight {
    @Id
    @GeneratedValue
    private Long id;

    private String departure;
    private String arrival;
    private ZonedDateTime departureTime;
    private Integer freeSeats;
    private BigDecimal pricePerSeat;
    @OneToMany(mappedBy = "flight")
    private List<Reservation> reservations;

    public static Flight of(String departure, String arrival, ZonedDateTime departureTime, Integer freeSeats, BigDecimal pricePerSeat) {
        Flight flight = new Flight();
        flight.setDeparture(departure);
        flight.setArrival(arrival);
        flight.setDepartureTime(departureTime);
        flight.setFreeSeats(freeSeats);
        flight.setPricePerSeat(pricePerSeat);
        return flight;
    }

}
