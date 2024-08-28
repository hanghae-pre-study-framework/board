package com.hanghae.board.domain.post.service;


import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import java.util.Collections;
import java.util.List;
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
  void 전체게시글조회_존재하지않음() {
    // given
    doReturn(Collections.emptyList()).when(postRepository)
        .findAllByIsDestroyedOrderByCreatedAtDesc(false);

    // when
    final List<PostDto> post = postReadService.getPosts();

    // then
    Assertions.assertThat(post).isEmpty();
  }

  @Test
  void 전체게시글조회_작성일자내림차순() {
    // given
    Post post1 = createPost(1L);
    Post post2 = createPost(2L);
    doReturn(List.of(post2, post1)).when(postRepository)
        .findAllByIsDestroyedOrderByCreatedAtDesc(false);

    // when
    final List<PostDto> posts = postReadService.getPosts();

    // then
    Assertions.assertThat(posts).hasSize(2);
    Assertions.assertThat(posts.get(0).id()).isEqualTo(2L);
    Assertions.assertThat(posts.get(1).id()).isEqualTo(1L);
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
