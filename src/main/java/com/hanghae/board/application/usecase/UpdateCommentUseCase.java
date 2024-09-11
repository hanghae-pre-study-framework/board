package com.hanghae.board.application.usecase;

import com.hanghae.board.domain.comment.dto.CommentDetailDto;
import com.hanghae.board.domain.comment.dto.UpdateCommentCommand;
import com.hanghae.board.domain.comment.mapper.CommentMapper;
import com.hanghae.board.domain.comment.service.CommentWriteService;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.user.service.UserReadService;
import com.hanghae.board.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCommentUseCase {

  private final CommentWriteService commentWriteService;

  private final PostReadService postReadService;

  private final UserReadService userReadService;

  private final CommentMapper commentMapper;

  public CommentDetailDto execute(Long postId, Long commentId, UpdateCommentCommand command,
      UserPrincipal currentUser) {
    var post = postReadService.getPost(postId);
    var user = userReadService.getUser(post.getUserId());

    var comment = commentWriteService.updateComment(commentId, command, currentUser);

    return commentMapper.toCommentDetailDto(comment, user);
  }

}
