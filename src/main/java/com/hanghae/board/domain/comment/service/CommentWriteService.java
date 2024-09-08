package com.hanghae.board.domain.comment.service;

import com.hanghae.board.domain.comment.dto.CommentCommand;
import com.hanghae.board.domain.comment.dto.CommentDto;
import com.hanghae.board.domain.comment.entity.Comment;
import com.hanghae.board.domain.comment.mapper.CommentMapper;
import com.hanghae.board.domain.comment.repository.CommentRepository;
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
}
