package com.hanghae.board.domain.user.dto;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum UserRole implements GrantedAuthority {
  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");

  private final String authority;

  @Override
  public String getAuthority() {
    return this.authority;
  }
}