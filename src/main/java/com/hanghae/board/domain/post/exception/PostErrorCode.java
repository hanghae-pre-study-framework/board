package com.hanghae.board.domain.post.exception;

import com.hanghae.board.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

  POST_NOT_FOUND(404, "해당 게시글을 찾을 수 없습니다."),
  INVALID_POST_REQUEST(400, "잘못된 게시글 요청입니다.");

  private final int status;
  private final String message;

  @Override
  public String getCode() {
    return name();
  }

  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}