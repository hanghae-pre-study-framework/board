package com.hanghae.board.domain.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CommentDetailDto {

  @NotNull
  private final Long id;

  @NotEmpty
  private final String content;

  @NotNull
  private final Long postId;

  @NotNull
  private final Long userId;

  @NotNull
  private final LocalDateTime createdAt;

  @NotNull
  private final LocalDateTime updatedAt;

  @NotNull
  private final UserDto user;
}
