package com.hanghae.board.domain.post.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.error.BusinessException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class PostReadServiceTest {

  private final String TITLE = "title";
  private final String CONTENT = "content";
  private final String USERNAME = "username";
  private final String ENCODED_PASSWORD = "encodedPassword";

  @Spy
  private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
  @InjectMocks
  private PostReadService postReadService;
  @Mock
  private PostRepository postRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  void 전체게시글조회_실패_존재하지않음() {
    // given
    doReturn(Collections.emptyList()).when(postRepository)
        .findAllByIsDestroyedOrderByCreatedAtDesc(false);

    // when
    final List<PostDto> post = postReadService.getPosts();

    // then
    Assertions.assertThat(post).isEmpty();
  }

  @Test
  void 전체게시글조회_성공_작성일자내림차순() {
    // given
    Post post1 = createPost(1L);
    Post post2 = createPost(2L);
    doReturn(List.of(post2, post1)).when(postRepository)
        .findAllByIsDestroyedOrderByCreatedAtDesc(false);

    // when
    final List<PostDto> posts = postReadService.getPosts();

    // then
    Assertions.assertThat(posts).hasSize(3);
    Assertions.assertThat(posts.get(0).id()).isEqualTo(2L);
    Assertions.assertThat(posts.get(1).id()).isEqualTo(1L);
  }


  @Test
  void 단건게시글조회_실패_존재하지않음() {
    // given
    final Long nonExistentPostId = -1L;
    doReturn(Optional.empty()).when(postRepository)
        .findByIdAndIsDestroyed(nonExistentPostId, false);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postReadService.getPost(nonExistentPostId));

    // then
    Assertions.assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);
  }

  @Test
  void 단건게시글조회_실패_삭제된게시글() {
    // given
    final Long destroyedPostId = 1L;
    doReturn(Optional.empty()).when(postRepository)
        .findByIdAndIsDestroyed(destroyedPostId, false);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postReadService.getPost(destroyedPostId));

    // then
    Assertions.assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_PASSWORD_MISMATCH);
  }

  @Test
  void 단건게시글조회_성공() {
    // given
    final Long postId = 1L;
    Post post = createPost(postId);
    doReturn(Optional.of(post)).when(postRepository).findByIdAndIsDestroyed(postId, false);

    // when
    final PostDto result = postReadService.getPost(postId);

    // then
    Assertions.assertThat(result.id()).isEqualTo(postId);
    Assertions.assertThat(result.title()).isEqualTo(TITLE);
    Assertions.assertThat(result.content()).isEqualTo(CONTENT);
    Assertions.assertThat(result.username()).isEqualTo(USERNAME);
  }


  private Post createPost(Long id) {
    return Post.builder()
        .id(id)
        .title(TITLE)
        .content(CONTENT)
        .username(USERNAME)
        .password(ENCODED_PASSWORD)
        .build();
  }
}
