package com.slupicki.lideo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.misc.TimeProvider;
import com.slupicki.lideo.misc.TimeProviderImpl;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.model.Flight;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class MainConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // Do any additional configuration here
    return builder.build();
  }

  @Bean
  @ConditionalOnMissingBean(TimeProvider.class)
  public TimeProvider timeProvider() {
    return new TimeProviderImpl();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper()
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.setDateFormat(new StdDateFormat());
    return mapper;
  }

  @Bean
  CommandLineRunner initDatabase(
      ClientRepository clientRepository,
      FlightRepository flightRepository
  ) {
    return args -> {
      log.info("Preloading " + clientRepository.save(Client.builder().login("client1").password("pass1").build()));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Wroclaw").arrival("Warsaw").departureTime(ZonedDateTime.now().plusDays(10)).freeSeats(10)
              .pricePerSeat(BigDecimal.valueOf(10)).build()
      ));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Bydgoszcz").arrival("Warsaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10)
              .pricePerSeat(BigDecimal.valueOf(10)).build()
      ));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Gdansk").arrival("Wroclaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10)
              .pricePerSeat(BigDecimal.valueOf(10)).build()
      ));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Warsaw").arrival("Wroclaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10)
              .pricePerSeat(BigDecimal.valueOf(10)).build()
      ));
    };
  }
}
