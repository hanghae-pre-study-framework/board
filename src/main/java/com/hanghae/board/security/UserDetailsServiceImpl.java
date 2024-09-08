package com.hanghae.board.security;

import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.repository.UserRepository;
import com.hanghae.board.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    return new UserPrincipal(userRepository.findByUsername(username)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND)));
  }
  
  public UserDetails loadUserById(Long id) {
    return new UserPrincipal(userRepository.findById(id)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND)));
  }
}