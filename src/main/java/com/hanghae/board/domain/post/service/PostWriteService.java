package com.hanghae.board.domain.post.service;


import com.hanghae.board.domain.post.dto.DeletePostCommand;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.error.BusinessException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostWriteService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final PasswordEncoder passwordEncoder;

  public PostDto createPost(PostCommand postCommand) {

    var post = Post
        .builder()
        .title(postCommand.title())
        .content(postCommand.content())
        .username(postCommand.username())
        .password(passwordEncoder.encode(postCommand.password()))
        .build();

    return postMapper.toDto(postRepository.save(post));
  }

  @Transactional
  public PostDto updatePost(Long id, UpdatePostCommand postCommand) {
    var post = postRepository.findWithPessimisticLockById(id)
        .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));

    if (!passwordEncoder.matches(postCommand.password(), post.getPassword())) {
      throw new BusinessException(PostErrorCode.POST_PASSWORD_MISMATCH);
    }

    post.update(postCommand.title(), postCommand.content(), postCommand.username(),
        passwordEncoder.encode(postCommand.password()), LocalDateTime.now());

    return postMapper.toDto(postRepository.save(post));
  }

  @Transactional
  public boolean deletePost(Long id, DeletePostCommand postCommand) {
    var post = postRepository.findWithPessimisticLockById(id)
        .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));

    if (!passwordEncoder.matches(postCommand.password(), post.getPassword())) {
      throw new BusinessException(PostErrorCode.POST_PASSWORD_MISMATCH);

    }

    if (post.isDestroyed()) {
      throw new BusinessException(PostErrorCode.POST_ALREADY_DELETED);
    }

    post.destroy(LocalDateTime.now());

    postRepository.save(post);

    return true;
  }
}
