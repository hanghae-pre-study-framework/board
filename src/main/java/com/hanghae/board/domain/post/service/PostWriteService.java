package com.hanghae.board.domain.post.service;


import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostWriteService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final PasswordEncoder passwordEncoder;

  public PostDto create(PostCommand postCommand) {

    var post = Post
        .builder()
        .title(postCommand.title())
        .content(postCommand.content())
        .username(postCommand.username())
        .password(passwordEncoder.encode(postCommand.password()))
        .build();

    return postMapper.toDto(postRepository.save(post));
  }

}
