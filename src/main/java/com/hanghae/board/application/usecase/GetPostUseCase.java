package com.hanghae.board.application.usecase;

import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.user.service.UserReadService;
import com.hanghae.board.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPostUseCase {

  private final PostReadService postReadService;

  private final PostMapper postMapper;

  private final UserReadService userReadService;

  public PostDto execute(Long postId) {
    var post = postReadService.getPost(postId);

    if (post.isDestroyed()) {
      throw new BusinessException(PostErrorCode.POST_DELETED);
    }

    var user = userReadService.getUser(post.getUserId());

    return postMapper.toDto(post, user);
  }
}
