package com.hanghae.board.domain.comment.repository;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;

import com.hanghae.board.domain.comment.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CommentRepositoryTest {

  @Autowired
  private CommentRepository target;

  @Test
  void 댓글_작성() {
    // given
    final Comment comment = SUT.giveMeBuilder(Comment.class)
        .setNull("id")
        .setNull("updatedAt")
        .sample();

    // when
    Comment savedComment = target.save(comment);

    // then
    assertThat(savedComment.getId()).isNotNull();
    assertThat(savedComment.getPostId()).isEqualTo(comment.getPostId());
    assertThat(savedComment.getContent()).isEqualTo(comment.getContent());
    assertThat(savedComment.getCreatedAt()).isNotNull();
    assertThat(savedComment.getUpdatedAt()).isNull();
  }

  @Test
  void 댓글_조회() {
    // given
    final Comment comment = SUT.giveMeBuilder(Comment.class)
        .setNull("id")
        .setNull("updatedAt")
        .sample();
    final Comment savedComment = target.save(comment);

    // when
    final Comment foundComment = target.findById(savedComment.getId()).orElseThrow();

    // then
    assertThat(foundComment.getId()).isEqualTo(savedComment.getId());
    assertThat(foundComment.getPostId()).isEqualTo(savedComment.getPostId());
    assertThat(foundComment.getContent()).isEqualTo(savedComment.getContent());
    assertThat(foundComment.getCreatedAt()).isEqualTo(savedComment.getCreatedAt());
    assertThat(foundComment.getUpdatedAt()).isEqualTo(savedComment.getUpdatedAt());
  }

  @Test
  void 댓글_조회_쓰기락() {
    // given
    final Comment comment = SUT.giveMeBuilder(Comment.class)
        .setNull("id")
        .setNull("updatedAt")
        .sample();
    final Comment savedComment = target.save(comment);

    // when
    final Comment foundComment = target.findWithPessimisticLockById(savedComment.getId())
        .orElseThrow();

    // then
    assertThat(foundComment.getId()).isEqualTo(savedComment.getId());
    assertThat(foundComment.getPostId()).isEqualTo(savedComment.getPostId());
    assertThat(foundComment.getContent()).isEqualTo(savedComment.getContent());
    assertThat(foundComment.getCreatedAt()).isEqualTo(savedComment.getCreatedAt());
    assertThat(foundComment.getUpdatedAt()).isEqualTo(savedComment.getUpdatedAt());
  }

}
