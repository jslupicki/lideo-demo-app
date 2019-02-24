package com.slupicki.lideo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {

  @Id
  @GenericGenerator(name = "UseIdOrGenerate", strategy = "com.slupicki.lideo.misc.UseIdOrGenerate")
  @GeneratedValue(generator = "UseIdOrGenerate")
  private Long id;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(optional = false)
  private Client client;
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(optional = false, cascade = CascadeType.MERGE)
  private Flight flight;
  private Integer seats;
  private BigDecimal price;
  private Boolean cancellation;
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
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
