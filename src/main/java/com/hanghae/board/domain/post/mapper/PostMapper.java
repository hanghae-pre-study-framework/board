package com.hanghae.board.domain.post.mapper;

import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

  public PostDto toDto(Post post) {
    return new PostDto(
        post.getId(),
        post.getTitle(),
        post.getContent(),
        post.getUsername(),
        post.getPassword(),
        post.getCreatedAt(),
        post.getUpdatedAt()
    );
  }
}