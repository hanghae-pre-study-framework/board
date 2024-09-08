package com.hanghae.board.domain.user.repository;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;

import com.hanghae.board.domain.user.dto.UserRole;
import com.hanghae.board.domain.user.entity.User;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository target;

  @Test
  void 유저등록() {
    // given
    User user = User.builder()
        .username("username")
        .password("password")
        .role(UserRole.USER)
        .build();

    // when
    final User savedUser = target.save(user);

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
    final User savedUser = target.save(user);

    // when
    final User foundUser = target.findById(savedUser.getId()).orElseThrow();

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
    final User savedUser = target.save(user);

    // when
    final User foundUser = target.findByUsername(savedUser.getUsername()).orElseThrow();

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
    target.save(user);

    // when
    final boolean exists = target.existsByUsername("username");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  void 유저목록조회ByIds() {
    // given
    AtomicLong counter = new AtomicLong(0);
    final List<User> users = SUT.giveMeBuilder(User.class)
        .setLazy("username", () -> "username" + counter.getAndIncrement())
        .set("password", "password")
        .sampleList(10);
    final List<User> savedUsers = target.saveAll(users);
    final List<Long> userIds = savedUsers.stream()
        .map(User::getId)
        .toList();

    // when
    final List<User> foundUsers = target.findAllById(userIds);

    // then
    assertThat(foundUsers).hasSize(10);
    assertThat(foundUsers).containsAll(savedUsers);
  }

}
