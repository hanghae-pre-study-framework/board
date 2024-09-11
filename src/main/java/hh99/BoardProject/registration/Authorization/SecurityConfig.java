package hh99.BoardProject.registration.Authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http
              //  .csrf().disable() //CSRF 비활성화(JWT 사용 시 주로 비활성화)
              //  .authorizeRequests()  >> 더이상 이 두개 사용되지않음.
                .csrf(csrf->csrf.disable()) // CSRF비활성화  .csrf().disable() 대신 람다식을 사용.
                .authorizeHttpRequests(auth -> auth  //authorizeRequests 대신 authorizeHttpRequests 사용.
                        .requestMatchers("/users/register", "/users/login", "/css/**", "/js/**").permitAll() //회원가입이랑 로그인은 허용하고! css나 js도 허용
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요.
                )
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //세션을 사용하지 않음 (JWT기반인증)


        return http.build();  //필터 체인 구성 후 반환환
    }
}
