package com.hanghae.board.application.controller;

import com.hanghae.board.domain.auth.dto.LoginDto;
import com.hanghae.board.domain.user.dto.UserCommand;
import com.hanghae.board.domain.user.service.UserWriteService;
import com.hanghae.board.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserWriteService userWriteService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/sign-up")
  public ResponseEntity<Boolean> signUp(@RequestBody @Valid UserCommand command) {
    Boolean result = userWriteService.createUser(command);

    return ResponseEntity.ok(result);
  }

  @PostMapping("/login")
  public ResponseEntity<Boolean> authenticateUser(@RequestBody LoginDto loginDto,
      HttpServletResponse response) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtTokenProvider.createToken(authentication);

    response.setHeader("Authorization", "Bearer " + jwt);

    return ResponseEntity.ok(true);
  }
}
