package com.slupicki.lideo.model;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

  private String departure;
  private String arrival;
  private ZonedDateTime fromDepartureTime;
  private ZonedDateTime toDepartureTime;
  private Integer seats;

}
