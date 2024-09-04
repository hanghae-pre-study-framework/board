package com.hanghae.board.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanghae.board.domain.user.dto.UserRole;
import com.hanghae.board.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void 유저등록() {
    // given
    User user = User.builder()
        .username("username")
        .password("password")
        .role(UserRole.USER)
        .build();

    // when
    final User savedUser = userRepository.save(user);

    // then
    assertThat(savedUser.getId()).isNotNull();
    assertThat(savedUser.getUsername()).isEqualTo("username");
    assertThat(savedUser.getPassword()).isEqualTo("password");
    assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);
  }

  @Test
  void 유저조회ById() {
    // given
    User user = User.builder()
        .username("username")
        .password("password")
        .role(UserRole.USER)
        .build();
    final User savedUser = userRepository.save(user);

    // when
    final User foundUser = userRepository.findById(savedUser.getId()).orElseThrow();

    // then
    assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
    assertThat(foundUser.getUsername()).isEqualTo(savedUser.getUsername());
    assertThat(foundUser.getPassword()).isEqualTo(savedUser.getPassword());
    assertThat(foundUser.getRole()).isEqualTo(savedUser.getRole());
  }

  @Test
  void 유저조회ByUsername() {
    // given
    User user = User.builder()
        .username("username")
        .password("password")
        .role(UserRole.USER)
        .build();
    final User savedUser = userRepository.save(user);

    // when
    final User foundUser = userRepository.findByUsername(savedUser.getUsername()).orElseThrow();

    // then
    assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
    assertThat(foundUser.getUsername()).isEqualTo(savedUser.getUsername());
    assertThat(foundUser.getPassword()).isEqualTo(savedUser.getPassword());
    assertThat(foundUser.getRole()).isEqualTo(savedUser.getRole());
  }

  @Test
  void 유저이름중복체크() {
    // given
    User user = User.builder()
        .username("username")
        .password("password")
        .role(UserRole.USER)
        .build();
    userRepository.save(user);

    // when
    final boolean exists = userRepository.existsByUsername("username");

    // then
    assertThat(exists).isTrue();
  }

}
