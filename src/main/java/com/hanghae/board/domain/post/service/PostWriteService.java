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
import com.hanghae.board.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostWriteService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final PasswordEncoder passwordEncoder;

  public PostDto createPost(@Valid PostCommand postCommand, UserPrincipal currentUser) {

    var post = Post
        .builder()
        .title(postCommand.getTitle())
        .content(postCommand.getContent())
        .username(currentUser.getUsername())
        .password(passwordEncoder.encode(postCommand.getPassword()))
        .build();

    return postMapper.toDto(postRepository.save(post));
  }

  @Transactional
  public PostDto updatePost(Long id, UpdatePostCommand postCommand) {
    var post = postRepository.findWithPessimisticLockById(id)
        .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));

    if (post.isDestroyed()) {
      throw new BusinessException(PostErrorCode.POST_ALREADY_DELETED);
    }

    if (!passwordEncoder.matches(postCommand.getPassword(), post.getPassword())) {
      throw new BusinessException(PostErrorCode.POST_PASSWORD_MISMATCH);
    }

    post.update(postCommand.getTitle(), postCommand.getContent(), postCommand.getUsername(),
        passwordEncoder.encode(postCommand.getPassword()));

    return postMapper.toDto(postRepository.save(post));
  }

  @Transactional
  public boolean deletePost(Long id, DeletePostCommand postCommand) {
    var post = postRepository.findWithPessimisticLockById(id)
        .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));

    if (post.isDestroyed()) {
      throw new BusinessException(PostErrorCode.POST_ALREADY_DELETED);
    }

    if (!passwordEncoder.matches(postCommand.getPassword(), post.getPassword())) {
      throw new BusinessException(PostErrorCode.POST_PASSWORD_MISMATCH);

    }

    post.destroy();

    postRepository.save(post);

    return true;
  }
}
