package com.hanghae.board.domain.post.service;


import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostReadService {

  private final PostRepository postRepository;

  public List<Post> getPosts() {
    return postRepository.findAllByOrderByCreatedAtDesc();
  }
}
