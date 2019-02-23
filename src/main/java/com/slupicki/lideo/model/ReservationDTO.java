package com.slupicki.lideo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

  @NonNull
  private Long clientId;
  @NonNull
  private Long flightId;
  @NonNull
  private Integer seats;
  @NonNull
  private Boolean fasterCheckIn;
}
