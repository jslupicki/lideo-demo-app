package com.slupicki.lideo.model;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
  @OneToOne(cascade = CascadeType.ALL)
  private Payment payment;

  public static Reservation of(Client client, Flight flight, Integer seats, BigDecimal price) {
    return Reservation.builder()
        .client(client)
        .flight(flight)
        .seats(seats)
        .price(price)
        .cancellation(false)
        .build();
  }

  public static Reservation of(Client client, Flight flight, Integer seats, BigDecimal price, Boolean cancellation, Payment payment) {
    return Reservation.builder()
        .client(client)
        .flight(flight)
        .seats(seats)
        .price(price)
        .cancellation(cancellation)
        .payment(payment)
        .build();
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
