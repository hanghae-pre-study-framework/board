package com.hanghae.board.domain.post.exception;

import com.hanghae.board.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
  POST_DELETED(HttpStatus.NOT_FOUND, "삭제된 게시글입니다."),
  POST_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 게시글입니다."),
  POST_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
  POST_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),
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