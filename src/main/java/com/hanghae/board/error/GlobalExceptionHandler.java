package com.hanghae.board.error;

import com.hanghae.board.domain.auth.exception.AuthErrorCode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({BusinessException.class})
  public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception) {
    log.warn("BusinessException occur: ", exception);
    return this.makeErrorResponseEntity(exception.getErrorCode());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException exception) {
    return this.makeErrorResponseEntity(AuthErrorCode.BAD_CREDENTIALS);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
    log.warn("Exception occur: ", exception);
    return this.makeErrorResponseEntity(CommonErrorCode.UNKNOWN_EXCEPTION);
  }

  private ResponseEntity<ErrorResponse> makeErrorResponseEntity(
      final ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(ErrorResponse.of(errorCode));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    final List<String> errorList = ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();

    log.warn("Invalid DTO Parameter errors : {}", errorList);
    return this.makeErrorResponseEntity(errorList.toString());
  }

  private ResponseEntity<Object> makeErrorResponseEntity(final String errorDescription) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.toString())
            .message(errorDescription)
            .build());
  }

}