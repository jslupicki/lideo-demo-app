package com.slupicki.lideo.config;

import com.slupicki.lideo.dao.ClientRepository;
import com.slupicki.lideo.dao.UserRepository;
import com.slupicki.lideo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MainConfig {

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
