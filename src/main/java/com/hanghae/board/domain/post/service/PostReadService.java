package com.hanghae.board.domain.post.service;


import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.error.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostReadService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;

  public List<PostDto> getPosts() {
    return postRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(postMapper::toDto)
        .toList();
  }

  public PostDto getPost(Long id) {
    return postMapper.toDto(postRepository.findById(id)
        .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND)));
  }
}
