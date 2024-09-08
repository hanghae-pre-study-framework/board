package com.hanghae.board.application.usecase;

import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.user.service.UserReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPostsUseCase {

  private final PostReadService postReadService;

  private final UserReadService userReadService;

  private final PostMapper postMapper;

  public List<PostDto> execute() {
    var posts = postReadService.getPosts();
    var userIds = posts.stream()
        .map(post -> post.getUserId())
        .toList();
    var users = userReadService.getUsers(userIds);

    return posts.stream()
        .map(post -> postMapper.toDto(post, users.stream()
            .filter(user -> user.getId().equals(post.getUserId()))
            .findFirst()
            .get()))
        .toList();
  }

}
