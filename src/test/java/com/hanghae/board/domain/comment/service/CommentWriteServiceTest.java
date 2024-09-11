package com.hanghae.board.domain.comment.service;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.comment.dto.CommentCommand;
import com.hanghae.board.domain.comment.dto.CommentDto;
import com.hanghae.board.domain.comment.dto.UpdateCommentCommand;
import com.hanghae.board.domain.comment.entity.Comment;
import com.hanghae.board.domain.comment.exception.CommentErrorCode;
import com.hanghae.board.domain.comment.mapper.CommentMapper;
import com.hanghae.board.domain.comment.repository.CommentRepository;
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
class CommentWriteServiceTest {

  @Spy
  private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
  @InjectMocks
  private CommentWriteService target;
  @Mock
  private CommentRepository commentRepository;

  @Test
  void 댓글작성_성공() {
    // given
    final User user = SUT.giveMeBuilder(User.class)
        .setNotNull("id")
        .sample();
    final UserPrincipal currentUser = new UserPrincipal(user);

    CommentCommand command = SUT.giveMeOne(CommentCommand.class);
    Comment comment = SUT.giveMeBuilder(Comment.class)
        .setNotNull("id")
        .set("content", command.getContent())
        .set("userId", user.getId())
        .setNull("updatedAt")
        .sample();
    doReturn(comment).when(commentRepository).save(any(Comment.class));

    // when
    final CommentDto result = target.createComment(comment.getPostId(), command, currentUser);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo(command.getContent());
    assertThat(result.getPostId()).isEqualTo(comment.getPostId());
    assertThat(result.getUserId()).isEqualTo(user.getId());
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNull();
  }

  @Test
  void 댓글수정_실패_존재하지않는댓글() {
    // given
    final User user = SUT.giveMeBuilder(User.class)
        .setNotNull("id")
        .sample();
    final UserPrincipal currentUser = new UserPrincipal(user);
    final Long commentId = 1L;
    UpdateCommentCommand command = SUT.giveMeOne(UpdateCommentCommand.class);
    doReturn(Optional.empty()).when(commentRepository).findWithPessimisticLockById(commentId);

    // when
    final BusinessException exception = assertThrows(BusinessException.class,
        () -> target.updateComment(commentId, command, currentUser));

    // then
    assertThat(exception.getErrorCode()).isEqualTo(CommentErrorCode.COMMENT_NOT_FOUND);
  }

  @Test
  void 댓글수정_실패_UserId불일치() {
    // given
    final User user = SUT.giveMeBuilder(User.class)
        .setNotNull("id")
        .set("role", UserRole.USER)
        .sample();
    final UserPrincipal currentUser = new UserPrincipal(user);

    UpdateCommentCommand command = SUT.giveMeOne(UpdateCommentCommand.class);
    Comment comment = SUT.giveMeBuilder(Comment.class)
        .setNotNull("id")
        .setPostCondition("userId", Long.class, userId -> !user.getId().equals(userId))
        .sample();
    doReturn(Optional.of(comment)).when(commentRepository)
        .findWithPessimisticLockById(comment.getId());

    // when
    final BusinessException exception = assertThrows(BusinessException.class,
        () -> target.updateComment(comment.getId(), command, currentUser));

    // then
    assertThat(exception.getErrorCode()).isEqualTo(CommentErrorCode.COMMENT_UPDATE_FORBIDDEN);
  }

  @Test
  void 댓글수정_성공() {
    // given
    final User user = SUT.giveMeBuilder(User.class)
        .setNotNull("id")
        .set("role", UserRole.USER)
        .sample();
    final UserPrincipal currentUser = new UserPrincipal(user);

    UpdateCommentCommand command = SUT.giveMeOne(UpdateCommentCommand.class);
    Comment comment = SUT.giveMeBuilder(Comment.class)
        .setNotNull("id")
        .set("userId", user.getId())
        .setNotNull("updatedAt")
        .sample();
    doReturn(Optional.of(comment)).when(commentRepository)
        .findWithPessimisticLockById(comment.getId());
    doReturn(comment).when(commentRepository).save(any(Comment.class));

    // when
    final CommentDto result = target.updateComment(comment.getId(), command, currentUser);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo(command.getContent());
    assertThat(result.getPostId()).isEqualTo(comment.getPostId());
    assertThat(result.getUserId()).isEqualTo(user.getId());
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNotNull();
  }

  @Test
  void 댓글수정_성공_관리자() {
    // given
    final User user = SUT.giveMeBuilder(User.class)
        .setNotNull("id")
        .set("role", UserRole.ADMIN)
        .sample();
    final UserPrincipal currentUser = new UserPrincipal(user);

    UpdateCommentCommand command = SUT.giveMeOne(UpdateCommentCommand.class);
    Comment comment = SUT.giveMeBuilder(Comment.class)
        .setNotNull("id")
        .setPostCondition("userId", Long.class, userId -> !user.getId().equals(userId))
        .setNotNull("updatedAt")
        .sample();
    doReturn(Optional.of(comment)).when(commentRepository)
        .findWithPessimisticLockById(comment.getId());
    doReturn(comment).when(commentRepository).save(any(Comment.class));

    // when
    final CommentDto result = target.updateComment(comment.getId(), command, currentUser);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo(command.getContent());
    assertThat(result.getPostId()).isEqualTo(comment.getPostId());
    assertThat(result.getUserId()).isEqualTo(comment.getUserId());
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNotNull();
  }
}
