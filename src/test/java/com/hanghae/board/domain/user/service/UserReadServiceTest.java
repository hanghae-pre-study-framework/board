package com.hanghae.board.domain.user.service;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.user.dto.UserRole;
import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.mapper.UserMapper;
import com.hanghae.board.domain.user.repository.UserRepository;
import com.hanghae.board.error.BusinessException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserReadServiceTest {

  @InjectMocks
  private UserReadService target;

  @Mock
  private UserRepository userRepository;

  @Spy
  private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  void 유저조회실패_유저존재하지않음() {
    // given
    final Long userId = 1L;
    doReturn(Optional.empty()).when(userRepository).findById(userId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.getUser(userId));

    // then
    assertThat(result.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
  }

  @Test
  void 유저조회성공() {
    // given
    final Long userId = 1L;
    final User user = User.builder()
        .id(userId)
        .username("username")
        .role(UserRole.USER)
        .build();
    doReturn(Optional.of(user)).when(userRepository).findById(userId);

    // when
    final UserDto result = target.getUser(userId);

    // then
    assertThat(result.getId()).isEqualTo(userId);
    assertThat(result.getUsername()).isEqualTo(user.getUsername());
    assertThat(result.getRole()).isEqualTo(user.getRole());
  }

  @Test
  void 유저목록조회성공_ByIds() {
    // given
    final List<User> users = SUT.giveMe(User.class, 10);
    final List<Long> userIds = users.stream()
        .map(User::getId)
        .toList();
    doReturn(users).when(userRepository).findAllById(userIds);

    // when
    final List<UserDto> result = target.getUsers(userIds);

    // then
    assertThat(result).hasSize(10);
    assertThat(result).usingRecursiveComparison()
        .isEqualTo(userMapper.toDtos(users));
  }
}
