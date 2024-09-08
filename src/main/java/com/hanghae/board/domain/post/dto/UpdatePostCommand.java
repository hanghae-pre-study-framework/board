package com.hanghae.board.domain.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UpdatePostCommand {

  @NotEmpty
  private final String title;

  @NotEmpty
  private final String content;

}
