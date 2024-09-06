package com.hanghae.board.domain.post.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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


@ExtendWith(MockitoExtension.class)
class PostWriteServiceTest {

  private final String TITLE = "title";
  private final String CONTENT = "content";
  private final String USERNAME = "username";

  @Spy
  private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
  @InjectMocks
  private PostWriteService target;
  @Mock
  private PostRepository postRepository;

  @Test
  void 게시글단건생성_성공() {
    // given
    Post savedPost = post();
    PostCommand postCommand = PostCommand.builder()
        .title(TITLE)
        .content(CONTENT)
        .build();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build());
    doReturn(savedPost).when(postRepository).save(any(Post.class));

    // when
    PostDto result = target.createPost(postCommand, currentuser);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo(postCommand.getTitle());
    assertThat(result.getContent()).isEqualTo(postCommand.getContent());
    assertThat(result.getUsername()).isEqualTo(savedPost.getUsername());

    // verify
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
        .build();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build());
    doReturn(Optional.empty()).when(postRepository).findWithPessimisticLockById(nonExistentPostId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.updatePost(nonExistentPostId, updatePostCommand, currentuser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);
  }

  @Test
  void 게시글수정_실패_유저네임불일치() {
    // given
    Long postId = 1L;
    Post post = post();
    UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
        .title(TITLE)
        .content(CONTENT)
        .build();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username("anotherUser")
        .role(UserRole.USER)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.updatePost(postId, updatePostCommand, currentuser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_UPDATE_FORBIDDEN);
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
        .build();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.updatePost(postId, updatePostCommand, currentuser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_ALREADY_DELETED);
  }

  @Test
  void 게시글수정_성공_일반유저() {
    // given
    Long postId = 1L;
    Post post = post();
    UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
        .title("UPDATED " + TITLE)
        .content("UPDATED " + CONTENT)
        .build();
    Post updatedPost = Post.builder()
        .id(postId)
        .title(updatePostCommand.getTitle())
        .content(updatePostCommand.getContent())
        .build();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);
    doReturn(updatedPost).when(postRepository).save(post);

    // when
    PostDto result = target.updatePost(postId, updatePostCommand, currentuser);

    // then
    assertThat(result.getId()).isEqualTo(postId);
    assertThat(result.getTitle()).isEqualTo(updatePostCommand.getTitle());
    assertThat(result.getContent()).isEqualTo(updatePostCommand.getContent());
    assertThat(result.getUsername()).isEqualTo(updatedPost.getUsername());

    // verify
    verify(postRepository, times(1)).save(post);
    verify(postMapper, times(1)).toDto(updatedPost);
  }

  @Test
  void 게시글수정_성공_관리자() {
    // given
    Long postId = 1L;
    Post post = post();
    UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
        .title("UPDATED " + TITLE)
        .content("UPDATED " + CONTENT)
        .build();
    Post updatedPost = Post.builder()
        .id(postId)
        .title(updatePostCommand.getTitle())
        .content(updatePostCommand.getContent())
        .build();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username("anotherUser")
        .role(UserRole.ADMIN)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);
    doReturn(updatedPost).when(postRepository).save(post);

    // when
    PostDto result = target.updatePost(postId, updatePostCommand, currentuser);

    // then
    assertThat(result.getId()).isEqualTo(postId);
    assertThat(result.getTitle()).isEqualTo(updatePostCommand.getTitle());
    assertThat(result.getContent()).isEqualTo(updatePostCommand.getContent());
    assertThat(result.getUsername()).isEqualTo(updatedPost.getUsername());

    // verify
    verify(postRepository, times(1)).save(post);
    verify(postMapper, times(1)).toDto(updatedPost);
  }

  @Test
  void 게시글삭제_실패_게시글없음() {
    // given
    Long nonExistentPostId = 1L;
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build());
    doReturn(Optional.empty()).when(postRepository).findWithPessimisticLockById(nonExistentPostId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.deletePost(nonExistentPostId, currentuser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);

    // verify
    verify(postRepository, times(1)).findWithPessimisticLockById(nonExistentPostId);
  }

  @Test
  void 게시글삭제_실패_유저네임불일치() {
    // given
    Long postId = 1L;
    Post post = post();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username("anotherUser")
        .role(UserRole.USER)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.deletePost(postId, currentuser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_DELETE_FORBIDDEN);

    // verify
    verify(postRepository, times(1)).findWithPessimisticLockById(postId);
  }

  @Test
  void 게시글삭제_실패_게시글삭제됨() {
    // given
    Long postId = 1L;
    Post post = post();
    post.destroy();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.deletePost(postId, currentuser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_ALREADY_DELETED);

    // verify
    verify(postRepository, times(1)).findWithPessimisticLockById(postId);
  }

  @Test
  void 게시글삭제성공_일반유저() {
    // given
    Long postId = 1L;
    Post post = post();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username(USERNAME)
        .role(UserRole.USER)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final Boolean result = target.deletePost(postId, currentuser);

    // then
    assertThat(result).isTrue();

    // verify
    verify(postRepository, times(1)).save(post);
  }

  @Test
  void 게시글삭제성공_관리자() {
    // given
    Long postId = 1L;
    Post post = post();
    UserPrincipal currentuser = new UserPrincipal(User.builder()
        .id(1L)
        .username("anotherUser")
        .role(UserRole.ADMIN)
        .build());
    doReturn(Optional.of(post)).when(postRepository).findWithPessimisticLockById(postId);

    // when
    final Boolean result = target.deletePost(postId, currentuser);

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
        .build();
  }
}
