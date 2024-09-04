package com.hanghae.board.security;

import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.repository.UserRepository;
import com.hanghae.board.error.BusinessException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

    // TODO: Add roles and authorities
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
    log.info("User {} loaded with default authority: ROLE_USER", username);

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities(Collections.singletonList(authority))
        .build();
  }
}