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

}
