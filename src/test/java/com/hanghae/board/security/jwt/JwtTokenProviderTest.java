package com.hanghae.board.security.jwt;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.security.UserPrincipal;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

  private final String SECRET = "testsecrettestsecrettestsecrettestsecrettestsecret";
  private final long EXPIRATION = 3600; // 1 hour
  private JwtTokenProvider target;
  @Mock
  private Authentication authentication;

  @BeforeEach
  void init() {
    target = new JwtTokenProvider(SECRET, EXPIRATION);
  }

  @Test
  void 토큰생성_성공() {
    // given
    User user = SUT.giveMeBuilder(User.class).setNotNull("id").sample();
    doReturn(new UserPrincipal(user)).when(authentication).getPrincipal();
    doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
        .when(authentication).getAuthorities();

    // when
    final String result = target.createToken(authentication);

    // then
    assertThat(result).isNotBlank();
    assertThat(target.validateToken(result)).isTrue();
    assertThat(target.getUserIdFromToken(result)).isEqualTo(user.getId());
  }

  @Test
  void 토큰검증_실패_만료된토큰() throws InterruptedException {
    // given
    JwtTokenProvider shortLivedProvider = new JwtTokenProvider(SECRET,
        1);
    User user = SUT.giveMeBuilder(User.class).setNotNull("id").sample();
    doReturn(new UserPrincipal(user)).when(authentication).getPrincipal();
    doReturn(Collections.emptyList()).when(authentication).getAuthorities();
    String token = shortLivedProvider.createToken(authentication);

    // when
    Thread.sleep(1000);
    final boolean result = shortLivedProvider.validateToken(token);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void 토큰사용자ID조회_실패_유효하지않은토큰() {
    // given
    String invalidToken = "invalidToken";

    // when
    final Exception result = assertThrows(Exception.class,
        () -> target.getUserIdFromToken(invalidToken));

    // then
    assertThat(result).isInstanceOf(Exception.class);
  }
}