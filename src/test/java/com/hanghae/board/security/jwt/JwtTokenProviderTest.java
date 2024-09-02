package com.hanghae.board.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

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
  private JwtTokenProvider jwtTokenProvider;
  @Mock
  private Authentication authentication;

  @BeforeEach
  void init() {
    jwtTokenProvider = new JwtTokenProvider(SECRET, EXPIRATION);
  }

  @Test
  void 토큰생성_성공() {
    // given
    doReturn("testUser").when(authentication).getName();
    doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
        .when(authentication).getAuthorities();

    // when
    final String result = jwtTokenProvider.createToken(authentication);

    // then
    assertThat(result).isNotBlank();
    assertThat(jwtTokenProvider.validateToken(result)).isTrue();
    assertThat(jwtTokenProvider.getUsernameFromToken(result)).isEqualTo("testUser");
  }

  @Test
  void 토큰_인증정보조회_성공() {
    // given
    doReturn("testUser").when(authentication).getName();
    doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
        .when(authentication).getAuthorities();
    String token = jwtTokenProvider.createToken(authentication);

    // when
    final Authentication result = jwtTokenProvider.getAuthentication(token);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("testUser");
    assertThat(result.getAuthorities()).hasSize(1);
    assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
  }

  @Test
  void 토큰검증_실패_만료된토큰() throws InterruptedException {
    // given
    JwtTokenProvider shortLivedProvider = new JwtTokenProvider(SECRET,
        1);
    doReturn("testUser").when(authentication).getName();
    doReturn(Collections.emptyList()).when(authentication).getAuthorities();
    String token = shortLivedProvider.createToken(authentication);

    // when
    Thread.sleep(1000);
    final boolean result = shortLivedProvider.validateToken(token);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void 토큰사용자명조회_실패_유효하지않은토큰() {
    // given
    String invalidToken = "invalidToken";

    // when
    final Exception result = assertThrows(Exception.class,
        () -> jwtTokenProvider.getUsernameFromToken(invalidToken));

    // then
    assertThat(result).isInstanceOf(Exception.class);
  }
}