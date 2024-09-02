package com.hanghae.board.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long tokenExpirationMilliseconds;

  public JwtTokenProvider(
      @Value("${spring.jwt.secret}") String secret,
      @Value("${spring.jwt.expiration}") long expiration) {

    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.tokenExpirationMilliseconds = expiration * 1000;
  }

  public String createToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    final long now = (new Date()).getTime();
    Date expiryDate = new Date(now + this.tokenExpirationMilliseconds);

    return Jwts.builder()
        .subject(authentication.getName())
        .claim("auth", authorities)
        .issuedAt(new Date(now))
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();
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

    UserDetails principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      // 로그 처리
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}