package com.hanghae.board.domain.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hanghae.board.domain.user.dto.UserCommand;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.repository.UserRepository;
import com.hanghae.board.error.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserWriteServiceTest {

  @InjectMocks
  private UserWriteService target;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;


  @Test
  void 유저등록_실패_유저이름중복() {
    // given
    final UserCommand command = UserCommand.builder()
        .username("username")
        .password("password")
        .build();
    doReturn(true).when(userRepository).existsByUsername(command.getUsername());

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.createUser(command)
    );

    // then
    assertThat(result.getErrorCode()).isEqualTo(UserErrorCode.USERNAME_DUPLICATION);
  }

  @Test
  void 유저등록_성공() {
    // given
    final UserCommand command = UserCommand.builder()
        .username("username")
        .password("password")
        .build();
    doReturn("encodedPassword").when(passwordEncoder).encode(command.getPassword());

    // when
    final boolean result = target.createUser(command);

    // then
    assertThat(result).isTrue();

    // verify
    verify(passwordEncoder, times(1)).encode(command.getPassword());
    verify(userRepository).existsByUsername(command.getUsername());
    verify(userRepository).save(
        argThat(user -> user.getUsername().equals(command.getUsername())
            && user.getPassword().equals("encodedPassword"))
    );
  }

}
