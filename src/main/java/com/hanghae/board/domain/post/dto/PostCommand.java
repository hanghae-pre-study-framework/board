package com.hanghae.board.domain.post.dto;

public record PostCommand(
    String title,
    String content,
    String username,
    String password
) {

}
