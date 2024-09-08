package com.hanghae.board.domain.post.service;


import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.domain.user.dto.UserRole;
import com.hanghae.board.error.BusinessException;
import com.hanghae.board.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostWriteService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;

  @Transactional
  public PostDto createPost(@Valid PostCommand postCommand, UserPrincipal currentUser) {

    var post = Post
        .builder()
        .title(postCommand.getTitle())
        .content(postCommand.getContent())
        .userId(currentUser.getUser().getId())
        .build();

    return postMapper.toDto(postRepository.save(post));
  }

  @Transactional
  public PostDto updatePost(Long id, UpdatePostCommand postCommand, UserPrincipal currentUser) {
    var post = postRepository.findWithPessimisticLockById(id)
        .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));

    if (post.isDestroyed()) {
      throw new BusinessException(PostErrorCode.POST_ALREADY_DELETED);
    }

    boolean isAdmin = UserRole.ADMIN.equals(currentUser.getUser().getRole());

    if (!isAdmin && !post.getUserId().equals(currentUser.getUser().getId())) {
      throw new BusinessException(PostErrorCode.POST_UPDATE_FORBIDDEN);
    }

    post.update(postCommand.getTitle(), postCommand.getContent());

    return postMapper.toDto(postRepository.save(post));
  }

  @Transactional
  public boolean deletePost(Long id, UserPrincipal currentUser) {
    var post = postRepository.findWithPessimisticLockById(id)
        .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));

    if (post.isDestroyed()) {
      throw new BusinessException(PostErrorCode.POST_ALREADY_DELETED);
    }

    boolean isAdmin = UserRole.ADMIN.equals(currentUser.getUser().getRole());

    if (!isAdmin && !post.getUserId().equals(currentUser.getUser().getId())) {
      throw new BusinessException(PostErrorCode.POST_DELETE_FORBIDDEN);
    }

    post.destroy();

    postRepository.save(post);

    return true;
  }
}
