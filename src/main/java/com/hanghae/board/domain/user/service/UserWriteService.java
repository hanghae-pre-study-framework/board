package com.hanghae.board.domain.user.service;

import com.hanghae.board.domain.user.dto.UserCommand;
import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.repository.UserRepository;
import com.hanghae.board.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteService {

  private final UserRepository userRepository;

  public boolean createUser(UserCommand command) {

    final boolean exists = userRepository.existsByUsername(command.getUsername());

    if (exists) {
      throw new BusinessException(UserErrorCode.USERNAME_DUPLICATION);
    }

    userRepository.save(User.builder()
        .username(command.getUsername())
        .password(command.getPassword())
        .build());

    return true;
  }
}
