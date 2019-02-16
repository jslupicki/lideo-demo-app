package com.slupicki.lideo.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Client client;
    @ManyToOne(optional = false)
    private Flight flight;
    private Integer seats;
    private BigDecimal price;
    private Boolean cancellation;
    @OneToOne
    private Payment payment;

    public static Reservation of(Client client, Flight flight, Integer seats, BigDecimal price) {
        return of(client, flight, seats, price, false, null);
    }

    public static Reservation of(Client client, Flight flight, Integer seats, BigDecimal price, Boolean cancellation, Payment payment) {
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setFlight(flight);
        reservation.setSeats(seats);
        reservation.setPrice(price);
        reservation.setCancellation(cancellation);
        reservation.setPayment(payment);
        return reservation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("client", client.getId())
                .append("flight", flight.getId())
                .append("seats", seats)
                .append("price", price)
                .append("cancellation", cancellation)
                .append("payment", payment)
                .toString();
    }
}
