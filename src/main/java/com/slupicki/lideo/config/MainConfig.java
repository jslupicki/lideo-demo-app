package com.slupicki.lideo.config;

import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.FlightRepository;
import com.slupicki.lideo.dao.UserRepository;
import com.slupicki.lideo.misc.TimeProvider;
import com.slupicki.lideo.misc.TimeProviderImpl;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.User;
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
  CommandLineRunner initDatabase(
      UserRepository userRepository,
      ClientRepository clientRepository,
      FlightRepository flightRepository
  ) {
    return args -> {
      log.info("Preloading " + userRepository.save(new User("login", "pass")));
      log.info("Preloading " + clientRepository.save(Client.builder().login("client1").password("pass1").build()));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Wroclaw").arrival("Warsaw").departureTime(ZonedDateTime.now().plusDays(3)).freeSeats(10).build()
      ));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Bydgoszcz").arrival("Warsaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10).build()
      ));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Gdansk").arrival("Wroclaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10).build()
      ));
      log.info("Preloading " + flightRepository.save(
          Flight.builder().departure("Warsaw").arrival("Wroclaw").departureTime(ZonedDateTime.now().minusDays(5)).freeSeats(10).build()
      ));
    };
  }
}
