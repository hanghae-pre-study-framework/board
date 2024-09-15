package hh99.BoardProject.registration.Authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity//시큐리티 등록
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private final static List<String> AUTH_WHITELIST = List.of(
                "/auth"
            ,   "/auth/login"
            ,   "/css/**"
            ,   "/js/**"
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http
              //  .csrf().disable() //CSRF 비활성화(JWT 사용 시 주로 비활성화)
              //  .authorizeRequests()  >> 더이상 이 두개 사용되지않음.
                .csrf(csrf->csrf.disable()) // CSRF비활성화  .csrf().disable() 대신 람다식을 사용.
                .authorizeHttpRequests(auth -> auth  //authorizeRequests 대신 authorizeHttpRequests 사용.
                        .requestMatchers(request-> AUTH_WHITELIST.contains(request.getRequestURI())).permitAll() //회원가입이랑 로그인은 허용하고! css나 js도 허용
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요.
                )
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //세션을 사용하지 않음 (JWT기반인증)
    // 세션 사용 시
  //http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false);


        return http.build();  //필터 체인 구성 후 반환환
    }

    // AuthenticationManager 빈 등록
    /**
     * *AuthenticationManager**는 인증을 처리하는 역할을 하며, Spring Security 5 이상에서는 자동으로 빈으로 등록되지 않기 때문에 명시적으로 빈으로 등록해야 합니다.
     **AuthenticationConfiguration**을 사용해 getAuthenticationManager() 메서드로 AuthenticationManager를 반환합니다.
    * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
