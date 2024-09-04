package com.hanghae.board.domain.post.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hanghae.board.domain.post.dto.DeletePostCommand;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.domain.user.dto.UserRole;
import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.error.BusinessException;
import com.hanghae.board.security.UserPrincipal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;


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

  @Test
  @WithMockUser(roles = "USER")
  void 게시글단건생성_성공() {
    // given
    Post savedPost = post();
    PostCommand postCommand = PostCommand.builder()
        .title(TITLE)
        .content(CONTENT)
        .username(USERNAME)
        .password(PASSWORD)
        .build();
    User user = User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build();
    UserPrincipal currentuser = new UserPrincipal(user);
    doReturn(ENCODED_PASSWORD).when(passwordEncoder).encode(PASSWORD);
    doReturn(savedPost).when(postRepository).save(any(Post.class));

    // when
    PostDto result = postWriteService.createPost(postCommand, currentuser);

    // then
    assertThat(result.id()).isNotNull();
    assertThat(result.title()).isEqualTo(postCommand.getTitle());
    assertThat(result.content()).isEqualTo(postCommand.getContent());
    assertThat(result.username()).isEqualTo(postCommand.getUsername());

    // verify
    verify(passwordEncoder, times(1)).encode(PASSWORD);
    verify(postRepository, times(1)).save(any(Post.class));
    verify(postMapper, times(1)).toDto(savedPost);
  }

  @Test
  void 게시글수정_실패_게시글없음() {
    // given
    Long nonExistentPostId = 1L;
    UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
        .title(TITLE)
        .content(CONTENT)
        .username(USERNAME)
        .password(PASSWORD)
        .build();
    doReturn(Optional.empty()).when(postRepository).findWithPessimisticLockById(nonExistentPostId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postWriteService.updatePost(nonExistentPostId, updatePostCommand));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);
  }

  @Test
  void 게시글수정_실패_비밀번호불일치() {
    // given
    Long postId = 1L;
    Post post = post();
    UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
        .title(TITLE)
        .content(CONTENT)
        .username(USERNAME)
        .password(PASSWORD)
        .build();
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);
    doReturn(false).when(passwordEncoder).matches(PASSWORD, post.getPassword());

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postWriteService.updatePost(postId, updatePostCommand));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_PASSWORD_MISMATCH);
  }

  @Test
  void 게시글수정_실패_게시글삭제됨() {
    // given
    Long postId = 1L;
    Post post = post();
    post.destroy();

    UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
        .title(TITLE)
        .content(CONTENT)
        .username(USERNAME)
        .password(PASSWORD)
        .build();
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postWriteService.updatePost(postId, updatePostCommand));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_ALREADY_DELETED);
  }

  @Test
  void 게시글수정_성공() {
    // given
    Long postId = 1L;
    Post post = post();
    UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
        .title("UPDATED " + TITLE)
        .content("UPDATED " + CONTENT)
        .username("UPDATED " + USERNAME)
        .password(PASSWORD)
        .build();
    Post updatedPost = Post.builder()
        .id(postId)
        .title(updatePostCommand.getTitle())
        .content(updatePostCommand.getContent())
        .username(updatePostCommand.getUsername())
        .build();
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);
    doReturn(true).when(passwordEncoder).matches(PASSWORD, post.getPassword());
    doReturn(updatedPost).when(postRepository).save(post);

    // when
    PostDto result = postWriteService.updatePost(postId, updatePostCommand);

    // then
    assertThat(result.id()).isEqualTo(postId);
    assertThat(result.title()).isEqualTo(updatePostCommand.getTitle());
    assertThat(result.content()).isEqualTo(updatePostCommand.getContent());
    assertThat(result.username()).isEqualTo(updatePostCommand.getUsername());

    // verify
    verify(postRepository, times(1)).save(post);
    verify(postMapper, times(1)).toDto(updatedPost);
  }

  @Test
  void 게시글삭제_실패_게시글없음() {
    // given
    Long nonExistentPostId = 1L;
    DeletePostCommand deletePostCommand = DeletePostCommand.builder()
        .password(PASSWORD)
        .build();
    doReturn(Optional.empty()).when(postRepository).findWithPessimisticLockById(nonExistentPostId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postWriteService.deletePost(nonExistentPostId, deletePostCommand));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);

    // verify
    verify(postRepository, times(1)).findWithPessimisticLockById(nonExistentPostId);
  }

  @Test
  void 게시글삭제_실패_비밀번호불일치() {
    // given
    Long postId = 1L;
    Post post = post();
    DeletePostCommand deletePostCommand = DeletePostCommand.builder()
        .password(PASSWORD)
        .build();
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);
    doReturn(false).when(passwordEncoder).matches(PASSWORD, post.getPassword());

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postWriteService.deletePost(postId, deletePostCommand));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_PASSWORD_MISMATCH);

    // verify
    verify(postRepository, times(1)).findWithPessimisticLockById(postId);
  }

  @Test
  void 게시글삭제_실패_게시글삭제됨() {
    // given
    Long postId = 1L;
    Post post = post();
    post.destroy();
    DeletePostCommand deletePostCommand = DeletePostCommand.builder()
        .password(PASSWORD)
        .build();
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> postWriteService.deletePost(postId, deletePostCommand));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_ALREADY_DELETED);

    // verify
    verify(postRepository, times(1)).findWithPessimisticLockById(postId);
  }

  @Test
  void 게시글삭제성공() {
    // given
    Long postId = 1L;
    Post post = post();
    DeletePostCommand deletePostCommand = DeletePostCommand.builder()
        .password(PASSWORD)
        .build();
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);
    doReturn(true).when(passwordEncoder).matches(PASSWORD, post.getPassword());

    // when
    final Boolean result = postWriteService.deletePost(postId, deletePostCommand);

    // then
    assertThat(result).isTrue();

    // verify
    verify(postRepository, times(1)).save(post);
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
