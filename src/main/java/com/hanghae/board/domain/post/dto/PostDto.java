package com.hanghae.board.domain.post.dto;

import java.time.LocalDateTime;

public record PostDto(
    Long id,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
