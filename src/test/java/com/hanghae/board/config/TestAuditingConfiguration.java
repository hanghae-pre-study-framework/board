package com.hanghae.board.config;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;

@TestConfiguration
public class TestAuditingConfiguration {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.of("testUser");
  }

  @Bean
  @Primary
  public DateTimeProvider dateTimeProvider() {
    return new DateTimeProvider() {
      @Override
      public Optional<TemporalAccessor> getNow() {
        return Optional.empty();
      }
    };
  }
}