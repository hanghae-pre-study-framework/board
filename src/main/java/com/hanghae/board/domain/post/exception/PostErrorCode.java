package com.hanghae.board.domain.post.exception;

import com.hanghae.board.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
  INVALID_POST_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 게시글 요청입니다."),
  POST_PASSWORD_MISMATCH(HttpStatus.FORBIDDEN, "게시글 비밀번호가 일치하지 않습니다."),
  POST_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 게시글입니다."),
  ;

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return name();
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}