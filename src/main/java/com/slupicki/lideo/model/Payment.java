package com.slupicki.lideo.model;

import java.math.BigDecimal;
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
public class Payment {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(optional = false)
  private Client client;
  @OneToOne(optional = false)
  private Reservation reservation;
  private BigDecimal amount;
  private Boolean paid;
  private String externalId;

  public static Payment of(Client client, Reservation reservation, BigDecimal amount) {
    return Payment.builder()
        .client(client)
        .reservation(reservation)
        .amount(amount)
        .paid(false)
        .build();
  }

  public static Payment of(Client client, Reservation reservation, BigDecimal amount, Boolean paid, String externalId) {
    return Payment.builder()
        .client(client)
        .reservation(reservation)
        .amount(amount)
        .paid(paid)
        .externalId(externalId)
        .build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("client", client.getId())
        .append("reservation", reservation.getId())
        .append("amount", amount)
        .append("paid", paid)
        .append("externalId", externalId)
        .toString();
  }
}
