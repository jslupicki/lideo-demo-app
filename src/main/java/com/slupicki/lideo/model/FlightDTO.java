package com.slupicki.lideo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class FlightDTO {

  private String departure;
  private String arrival;
  private ZonedDateTime fromDepartureTime;
  private ZonedDateTime toDepartureTime;
  private Integer seats;

}
