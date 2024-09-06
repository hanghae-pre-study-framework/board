package com.hanghae.board.domain.post.dto;

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

  private final Long id;
  private final String title;
  private final String content;
  private final String username;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
}
