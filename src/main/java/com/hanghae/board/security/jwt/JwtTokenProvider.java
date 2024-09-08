package com.hanghae.board.security.jwt;

import com.hanghae.board.domain.user.dto.UserRole;
import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long tokenExpirationMilliseconds;

  public JwtTokenProvider(
      @Value("${spring.jwt.secret}") String secret,
      @Value("${spring.jwt.expiration}") long expiration) {

    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.tokenExpirationMilliseconds = expiration * 1000;
    log.info("JwtTokenProvider initialized with expiration: {} seconds", expiration);
  }

  public String createToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    final long now = (new Date()).getTime();
    Date expiryDate = new Date(now + this.tokenExpirationMilliseconds);

    String token = Jwts.builder()
        .subject(authentication.getName())
        .claim("auth", authorities)
        .issuedAt(new Date(now))
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();

    log.debug("Token created for user: {}", authentication.getName());
    return token;
  }

  public Authentication getAuthentication(String token) {
    var claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();

    UserRole userRole = UserRole.valueOf(
        authorities.iterator().next().getAuthority().replace("ROLE_", "")
    );

    UserDetails principal = new UserPrincipal(
        User.builder()
            .username(claims.getSubject())
            .password("")
            .role(userRole)
            .build());

    log.debug("Authentication created for user: {}", claims.getSubject());
    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      log.debug("Token validated successfully");
      return true;
    } catch (Exception e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    String username = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    log.debug("Username extracted from token: {}", username);
    return username;
  }
}