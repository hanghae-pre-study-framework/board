package com.hanghae.board.domain.post.dto;

import java.util.Objects;

public record UpdatePostCommand(
    String title,
    String content,
    String username,
    String password
) {

  public UpdatePostCommand {
    Objects.requireNonNull(title);
    Objects.requireNonNull(content);
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
  }
}
