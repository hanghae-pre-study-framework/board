package com.hanghae.board.domain.user.application.controller;

import com.hanghae.board.domain.user.dto.UserCommand;
import com.hanghae.board.domain.user.service.UserWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserWriteService userWriteService;

  @PostMapping
  public ResponseEntity<Boolean> createUser(@RequestBody @Valid UserCommand command) {
    Boolean result = userWriteService.createUser(command);

    return ResponseEntity.ok(result);
  }

}
