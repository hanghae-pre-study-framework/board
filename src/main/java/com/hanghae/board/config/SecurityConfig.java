package com.hanghae.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authz ->
            authz
                // NOTE: Swagger UI 관련 요청은 인증 없이 허용 (개발 중에만 사용, 프로덕션에서는 제거해야 함)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                .permitAll()
                // NOTE: 임시로 모든 요청 허용 (개발 중에만 사용, 프로덕션에서는 제거해야 함)
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
        )
        .csrf(AbstractHttpConfigurer::disable);  // NOTE: CSRF 비활성화

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}