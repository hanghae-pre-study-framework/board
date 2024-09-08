package com.hanghae.board.domain.comment.service;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.comment.dto.CommentCommand;
import com.hanghae.board.domain.comment.dto.CommentDto;
import com.hanghae.board.domain.comment.entity.Comment;
import com.hanghae.board.domain.comment.mapper.CommentMapper;
import com.hanghae.board.domain.comment.repository.CommentRepository;
import com.hanghae.board.domain.user.entity.User;
import com.hanghae.board.security.UserPrincipal;
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
}
