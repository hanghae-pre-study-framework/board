package com.hanghae.board.domain.user.exception;

import com.hanghae.board.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
  USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 사용중인 유저 이름입니다."),
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