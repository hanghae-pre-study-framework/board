package com.hanghae.board.application;

import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.service.PostReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostReadService postReadService;

  @GetMapping
  public List<Post> getPosts() {
    return postReadService.getPosts();
  }
}
