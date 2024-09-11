package com.hanghae.board.security.jwt;

import com.hanghae.board.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long tokenExpirationMilliseconds;

  public JwtTokenProvider(
      @Value("${spring.jwt.secret}") String secret,
      @Value("${spring.jwt.expiration}") long expiration
  ) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.tokenExpirationMilliseconds = expiration * 1000;
    log.info("JwtTokenProvider initialized with expiration: {} seconds", expiration);
  }

  public String createToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    final long now = (new Date()).getTime();
    Date expiryDate = new Date(now + this.tokenExpirationMilliseconds);

    String token = Jwts.builder()
        .subject(userPrincipal.getId().toString())
        .claim("auth", authorities)
        .issuedAt(new Date(now))
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();

    log.debug("Token created for user: {}", authentication.getName());
    return token;
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

  public Long getUserIdFromToken(String token) {
    Long userId = Long.parseLong(Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject());

    log.debug("User ID extracted from token: {}", userId);
    return userId;
  }
}