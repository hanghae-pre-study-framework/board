package com.hanghae.board.domain.post.dto;

import com.hanghae.board.domain.comment.dto.UserDto;
import java.time.LocalDateTime;
import java.util.Optional;
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
  private final Long userId;
  private final Optional<UserDto> user;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
}
