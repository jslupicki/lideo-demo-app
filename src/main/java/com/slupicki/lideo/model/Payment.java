package com.slupicki.lideo.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

  @NonNull
  private BigDecimal amount;
  private Boolean paid;
  private String externalId;
  private ZonedDateTime createdAt;

  public static Payment of(BigDecimal amount) {
    return Payment.builder()
        .amount(amount)
        .paid(false)
        .createdAt(ZonedDateTime.now())
        .build();
  }

  public static Payment of(BigDecimal amount, Boolean paid, String externalId) {
    return Payment.builder()
        .amount(amount)
        .paid(paid)
        .externalId(externalId)
        .createdAt(ZonedDateTime.now())
        .build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("amount", amount)
        .append("paid", paid)
        .append("externalId", externalId)
        .toString();
  }
}
