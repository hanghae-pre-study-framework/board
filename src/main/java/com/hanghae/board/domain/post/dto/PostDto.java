package com.hanghae.board.domain.post.dto;

import com.hanghae.board.domain.comment.dto.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PostDto {

  @Positive
  private final Long id;

  @NotEmpty
  private final String title;

  @NotEmpty
  private final String content;

  @Positive
  private final Long userId;

  private final UserDto user;

  private final boolean isDestroyed;

  @NotNull
  @PastOrPresent
  private final LocalDateTime createdAt;

  private final LocalDateTime updatedAt;
}
