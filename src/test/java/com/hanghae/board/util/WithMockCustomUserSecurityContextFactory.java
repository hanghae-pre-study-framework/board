package com.hanghae.board.util;

import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    UserPrincipal principal = new UserPrincipal(
        User.builder()
            .id(1L)
            .username(customUser.username())
            .role(customUser.role())
            .build()
    );

    Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password",
        principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}
