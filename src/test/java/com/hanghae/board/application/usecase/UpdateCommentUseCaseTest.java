package com.hanghae.board.application.usecase;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.hanghae.board.domain.comment.dto.CommentDetailDto;
import com.hanghae.board.domain.comment.dto.CommentDto;
import com.hanghae.board.domain.comment.dto.UpdateCommentCommand;
import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.comment.mapper.CommentMapper;
import com.hanghae.board.domain.comment.service.CommentWriteService;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.service.UserReadService;
import com.hanghae.board.error.BusinessException;
import com.hanghae.board.security.UserPrincipal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCommentUseCaseTest {

  @InjectMocks
  private UpdateCommentUseCase target;

  @Mock
  private CommentWriteService commentWriteService;

  @Mock
  private PostReadService postReadService;

  @Mock
  private UserReadService userReadService;

  @Spy
  private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);


  @Test
  void 댓글수정_실패_게시글존재하지않음() {
    // given
    final Long postId = 1L;
    final Long commentId = 1L;
    final UpdateCommentCommand command = SUT.giveMeOne(UpdateCommentCommand.class);
    final UserPrincipal currentUser = new UserPrincipal(SUT.giveMeOne(User.class));
    doThrow(new BusinessException(PostErrorCode.POST_NOT_FOUND))
        .when(postReadService).getPost(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.execute(postId, commentId, command, currentUser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);
  }

  @Test
  void 댓글수정_실패_유저존재하지않음() {
    // given
    final Long commentId = 1L;
    final UpdateCommentCommand command = SUT.giveMeOne(UpdateCommentCommand.class);
    final UserPrincipal currentUser = new UserPrincipal(SUT.giveMeOne(User.class));
    final PostDto post = SUT.giveMeOne(PostDto.class);
    doReturn(post).when(postReadService).getPost(post.getId());
    doThrow(new BusinessException(UserErrorCode.USER_NOT_FOUND))
        .when(userReadService).getUser(post.getUserId());

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.execute(post.getId(), commentId, command, currentUser));

    // then
    assertThat(result.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
  }

  @Test
  void 댓글수정_성공() {
    // given
    final UpdateCommentCommand command = SUT.giveMeOne(UpdateCommentCommand.class);
    final User user = SUT.giveMeBuilder(User.class).setNotNull("id").sample();
    final UserDto userDto = SUT.giveMeBuilder(UserDto.class)
        .setNotNull("id")
        .set("username", user.getUsername())
        .set("role", user.getRole())
        .sample();
    final UserPrincipal currentUser = new UserPrincipal(user);
    final PostDto post = SUT.giveMeOne(PostDto.class);
    final CommentDto comment = SUT.giveMeBuilder(CommentDto.class)
        .setNotNull("id")
        .set("content", command.getContent())
        .set("postId", post.getId())
        .set("userId", user.getId())
        .set("createdAt", LocalDateTime.now())
        .setNotNull("updatedAt")
        .sample();
    doReturn(post).when(postReadService).getPost(post.getId());
    doReturn(userDto).when(userReadService).getUser(post.getUserId());
    doReturn(comment).when(commentWriteService)
        .updateComment(comment.getId(), command, currentUser);

    // when
    final CommentDetailDto result = target.execute(post.getId(), comment.getId(), command,
        currentUser);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo(command.getContent());
    assertThat(result.getPostId()).isEqualTo(post.getId());
    assertThat(result.getUserId()).isEqualTo(user.getId());
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNotNull();
  }
}
