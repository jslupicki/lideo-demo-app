package com.slupicki.lideo.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

  private final int registeredClientDiscount;
  private final int fasterCheckInFee;

  public PriceCalculator(
      @Value("${registration.registered.client.dicount.percent}")
          int registeredClientDiscount,
      @Value("${registration.faster.check.in.fee}")
          int fasterCheckInFee
  ) {
    this.registeredClientDiscount = registeredClientDiscount;
    this.fasterCheckInFee = fasterCheckInFee;
  }

  public BigDecimal calculateReservationPrice(
      BigDecimal pricePerSeat,
      int seatsToReservate,
      boolean registeredClient,
      boolean fasterCheckIn
  ) {
    BigDecimal result = pricePerSeat.multiply(BigDecimal.valueOf(seatsToReservate));
    if (registeredClient) {
      result = result.subtract(result.multiply(BigDecimal.valueOf(registeredClientDiscount)).divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_DOWN));
    }
    if (fasterCheckIn) {
      result = result.add(BigDecimal.valueOf(fasterCheckInFee));
    }
    return result;
  }
}
