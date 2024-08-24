package com.hanghae.board.domain.post.dto;

import java.time.LocalDateTime;

public record PostDto(
    Long id,
    String title,
    String content,
    String username,
    String password,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
