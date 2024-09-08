package com.hanghae.board.domain.comment.exception;

import com.hanghae.board.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CommentErrorCode implements ErrorCode {

  ;

  @Override
  public String getCode() {
    return this.name();
  }

  @Override
  public HttpStatus getStatus() {
    return this.getStatus();
  }

  @Override
  public String getMessage() {
    return this.getMessage();
  }
}
