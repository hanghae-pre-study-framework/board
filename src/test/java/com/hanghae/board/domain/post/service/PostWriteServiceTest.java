package com.hanghae.board.domain.post.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class PostWriteServiceTest {

  private final String TITLE = "title";
  private final String CONTENT = "content";
  private final String USERNAME = "username";
  private final String PASSWORD = "password";
  private final String ENCODED_PASSWORD = "encodedPassword";

  @Spy
  private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
  @InjectMocks
  private PostWriteService postWriteService;
  @Mock
  private PostRepository postRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @ParameterizedTest
  @NullSource
  void 게시글생성실패_NULL값(String nullValue) {
    assertThrows(NullPointerException.class, () -> {
      PostCommand postCommand = new PostCommand(nullValue, CONTENT, USERNAME, PASSWORD);
      postWriteService.createPost(postCommand);
    });

    assertThrows(NullPointerException.class, () -> {
      PostCommand postCommand = new PostCommand(TITLE, nullValue, USERNAME, PASSWORD);
      postWriteService.createPost(postCommand);
    });

    assertThrows(NullPointerException.class, () -> {
      PostCommand postCommand = new PostCommand(TITLE, CONTENT, nullValue, PASSWORD);
      postWriteService.createPost(postCommand);
    });

    assertThrows(NullPointerException.class, () -> {
      PostCommand postCommand = new PostCommand(TITLE, CONTENT, USERNAME, nullValue);
      postWriteService.createPost(postCommand);
    });
  }

  @Test
  void 게시글단건생성성공() {
    // given
    Post savedPost = post();
    PostCommand postCommand = new PostCommand(TITLE, CONTENT, USERNAME, PASSWORD);
    doReturn(ENCODED_PASSWORD).when(passwordEncoder).encode(PASSWORD);
    doReturn(savedPost).when(postRepository).save(any(Post.class));

    // when
    PostDto result = postWriteService.createPost(postCommand);

    // then
    Assertions.assertThat(result.id()).isNotNull();
    Assertions.assertThat(result.title()).isEqualTo(postCommand.title());
    Assertions.assertThat(result.content()).isEqualTo(postCommand.content());
    Assertions.assertThat(result.username()).isEqualTo(postCommand.username());

    // verify
    verify(passwordEncoder, times(1)).encode(PASSWORD);
    verify(postRepository, times(1)).save(any(Post.class));
    verify(postMapper, times(1)).toDto(savedPost);
  }

  private Post post() {
    return Post.builder()
        .id(-1L)
        .title(TITLE)
        .content(CONTENT)
        .username(USERNAME)
        .password(ENCODED_PASSWORD)
        .build();
  }
}
