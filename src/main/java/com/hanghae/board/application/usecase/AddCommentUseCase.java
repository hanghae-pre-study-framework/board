package com.hanghae.board.application.usecase;

import com.hanghae.board.domain.comment.dto.CommentCommand;
import com.hanghae.board.domain.comment.dto.CommentDetailDto;
import com.hanghae.board.domain.comment.mapper.CommentMapper;
import com.hanghae.board.domain.comment.service.CommentWriteService;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.user.service.UserReadService;
import com.hanghae.board.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddCommentUseCase {

  private final CommentWriteService commentWriteService;

  private final PostReadService postReadService;

  private final UserReadService userReadService;

  private final CommentMapper commentMapper;


  public CommentDetailDto execute(Long postId, CommentCommand command, UserPrincipal currentUser) {
    var post = postReadService.getPost(postId);
    var user = userReadService.getUser(post.getUserId());

    var comment = commentWriteService.createComment(post.getId(), command, currentUser);

    return commentMapper.toCommentDetailDto(comment, user);
  }
}
