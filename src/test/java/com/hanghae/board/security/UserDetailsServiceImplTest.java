package com.hanghae.board.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.repository.UserRepository;
import com.hanghae.board.error.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

  @InjectMocks
  private UserDetailsServiceImpl target;

  @Mock
  private UserRepository userRepository;

  @Test
  void 유저로드_실패() {
    // given
    String username = "username";
    doReturn(Optional.empty()).when(userRepository).findByUsername(username);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.loadUserByUsername(username));

    // then
    assertThat(result.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
  }

  @Test
  void 유저로드_성공() {
    // given
    String username = "username";
    doReturn(Optional.of(User.builder().username(username).password("password").build()))
        .when(userRepository).findByUsername(username);

    // when
    final UserDetails result = target.loadUserByUsername(username);

    // then
    assertThat(result.getUsername()).isEqualTo(username);
  }
}
