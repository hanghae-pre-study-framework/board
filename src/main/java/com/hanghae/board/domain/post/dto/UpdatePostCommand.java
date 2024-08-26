package com.hanghae.board.domain.post.dto;

public record UpdatePostCommand(
    String title,
    String content,
    String username,
    String password
) {

}
