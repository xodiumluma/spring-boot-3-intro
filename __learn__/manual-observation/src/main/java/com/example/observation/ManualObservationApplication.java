package com.example.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.ObservationTextPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class ManualObservationApplication {
  public static void main(String[] args) {
    SpringApplication.run(ManualObservationApplication.class, args);
  }

  @Bean
  ObservationTextPublisher otp() {
    return new ObservationTextPublisher();
  }

  @Bean
  ApplicationListener<ApplicationStartedEvent> doOnStart(ObservationRegistry registry) {
    return event -> generateString(registry);
  }

  void generateString(ObservationRegistry registry) {
    var something = Observation.createNotStarted("server.job", registry)
            .lowCardinalityKeyValue("jobType", "string")
            .observeChecked(() -> {
              log.info("Creating a string...");
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
                return "Nuthin!";
              }
              return "Somethin";
            });
    log.info("Result: " + something);
  }
}