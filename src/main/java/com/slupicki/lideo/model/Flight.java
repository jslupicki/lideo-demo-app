package com.slupicki.lideo.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flight {

  @Id
  @GenericGenerator(name = "UseIdOrGenerate", strategy = "com.slupicki.lideo.misc.UseIdOrGenerate")
  @GeneratedValue(generator = "UseIdOrGenerate")
  private Long id;

  private String departure;
  private String arrival;
  private ZonedDateTime departureTime;
  private Integer freeSeats;
  private BigDecimal pricePerSeat;
  @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
  private List<Reservation> reservations;

  public static Flight of(String departure, String arrival, ZonedDateTime departureTime, Integer freeSeats, BigDecimal pricePerSeat) {
    return Flight.builder()
        .departure(departure)
        .arrival(arrival)
        .departureTime(departureTime)
        .freeSeats(freeSeats)
        .pricePerSeat(pricePerSeat)
        .build();
  }

}
