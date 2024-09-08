package com.hanghae.board.application.controller;

import com.hanghae.board.application.usecase.AddCommentUseCase;
import com.hanghae.board.domain.comment.dto.CommentCommand;
import com.hanghae.board.domain.comment.dto.CommentDetailDto;
import com.hanghae.board.security.CurrentUser;
import com.hanghae.board.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

  private final AddCommentUseCase addCommentUseCase;

  @PostMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<CommentDetailDto> createComment(
      @PathVariable Long postId, @RequestBody @Valid CommentCommand command,
      @CurrentUser UserPrincipal currentUser) {

    var comment = addCommentUseCase.execute(postId, command, currentUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(comment);
  }

}

