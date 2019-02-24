package com.slupicki.lideo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ReservationDTO {

  @NonNull
  private Long clientId;
  @NonNull
  private Long flightId;
  @NonNull
  private Integer seats;
  private Boolean fasterCheckIn;
}
