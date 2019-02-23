package com.slupicki.lideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LideoDemoAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(LideoDemoAppApplication.class, args);
  }

}

