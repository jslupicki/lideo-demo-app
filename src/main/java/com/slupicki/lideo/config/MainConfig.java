package com.slupicki.lideo.config;

import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.UserRepository;
import com.slupicki.lideo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
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
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            ClientRepository clientRepository
    ) {
        return args -> {
            log.info("Preloading " + userRepository.save(new User("login", "pass")));
        };
    }
}
