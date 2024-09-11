package com.hanghae.board.domain.comment.service;

import com.hanghae.board.domain.comment.dto.CommentCommand;
import com.hanghae.board.domain.comment.dto.CommentDto;
import com.hanghae.board.domain.comment.dto.UpdateCommentCommand;
import com.hanghae.board.domain.comment.entity.Comment;
import com.hanghae.board.domain.comment.exception.CommentErrorCode;
import com.hanghae.board.domain.comment.mapper.CommentMapper;
import com.hanghae.board.domain.comment.repository.CommentRepository;
import com.hanghae.board.domain.user.dto.UserRole;
import com.hanghae.board.error.BusinessException;
import com.hanghae.board.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentWriteService {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  @Transactional
  public CommentDto createComment(Long postId, CommentCommand command, UserPrincipal currentUser) {
    Comment comment = Comment.builder()
        .content(command.getContent())
        .postId(postId)
        .userId(currentUser.getUser().getId())
        .build();

    return commentMapper.toDto(commentRepository.save(comment));
  }

  @Transactional
  public CommentDto updateComment(Long commentId, UpdateCommentCommand command,
      UserPrincipal currentUser) {
    Comment comment = commentRepository.findWithPessimisticLockById(commentId)
        .orElseThrow(() -> new BusinessException(CommentErrorCode.COMMENT_NOT_FOUND));

    final boolean isAdmin = currentUser.getUser().getRole().equals(UserRole.ADMIN);

    if (!isAdmin && !comment.getUserId().equals(currentUser.getUser().getId())) {
      throw new BusinessException(CommentErrorCode.COMMENT_UPDATE_FORBIDDEN);
    }

    comment.update(command.getContent());

    return commentMapper.toDto(commentRepository.save(comment));
  }
}
