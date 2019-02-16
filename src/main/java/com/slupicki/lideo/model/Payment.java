package com.slupicki.lideo.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
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
        return of(client, reservation, amount, false, null);
    }

    public static Payment of(Client client, Reservation reservation, BigDecimal amount, Boolean paid, String externalId) {
        Payment payment = new Payment();
        payment.setClient(client);
        payment.setReservation(reservation);
        payment.setAmount(amount);
        payment.setPaid(paid);
        payment.setExternalId(externalId);
        return payment;
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
