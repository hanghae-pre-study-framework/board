package com.hanghae.board.domain.user.service;

import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.mapper.UserMapper;
import com.hanghae.board.domain.user.repository.UserRepository;
import com.hanghae.board.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReadService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public UserDto getUser(Long userId) {
    return userMapper.toDto(userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND)));
  }

}
