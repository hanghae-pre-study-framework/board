package com.hanghae.board.util;

import com.hanghae.board.domain.user.dto.UserRole;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  String username() default "username";

  String name() default "Test User";

  UserRole role() default UserRole.USER;
}