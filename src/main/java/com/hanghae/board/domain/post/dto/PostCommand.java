package com.hanghae.board.domain.post.dto;

import java.util.Objects;

public record PostCommand(
    String title,
    String content,
    String username,
    String password
) {

  public PostCommand {
    Objects.requireNonNull(title);
    Objects.requireNonNull(content);
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
  }

}
