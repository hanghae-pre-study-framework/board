package com.hanghae.board.domain.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanghae.board.domain.post.entity.Post;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostRepositoryTest {

  @Autowired
  private PostRepository target;

  @Test
  void 게시글_전체_조회_내림차순() {
    // given
    final Post post1 = Post.builder()
        .title("제목1")
        .content("내용1")
        .username("작성자1")
        .build();
    final Post post2 = Post.builder()
        .title("제목2")
        .content("내용2")
        .username("작성자2")
        .build();
    final Post post3 = Post.builder()
        .title("제목3")
        .content("내용3")
        .username("작성자3")
        .build();
    target.save(post1);
    target.save(post2);
    target.save(post3);

    // when
    final List<Post> result = target.findAllByIsDestroyedOrderByCreatedAtDesc(false);

    // then
    assertThat(result).hasSize(3);
    assertThat(result).extracting("title")
        .containsExactly("제목3", "제목2", "제목1");
  }

  @Test
  void 게시글_등록() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .build();

    // when
    final Post result = target.save(post);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo("제목");
    assertThat(result.getContent()).isEqualTo("내용");
    assertThat(result.getUsername()).isEqualTo("작성자");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNull();
  }

  @Test
  void 게시글_삭제() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .build();
    final Post savedPost = target.save(post);

    // when
    target.delete(savedPost);

    // then
    assertThat(target.findById(savedPost.getId())).isEmpty();
  }

  @Test
  void 게시글_조회() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .build();
    final Post savedPost = target.save(post);

    // when
    final Post result = target.findById(savedPost.getId()).get();

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo("제목");
    assertThat(result.getContent()).isEqualTo("내용");
    assertThat(result.getUsername()).isEqualTo("작성자");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNull();
  }

  @Test
  void 게시글_조회_삭제_여부() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .build();
    final Post savedPost = target.save(post);

    // when
    final Post result = target.findByIdAndIsDestroyed(savedPost.getId(), false).get();

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo("제목");
    assertThat(result.getContent()).isEqualTo("내용");
    assertThat(result.getUsername()).isEqualTo("작성자");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNull();
  }

  @Test
  void 게시글_수정() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .build();
    final Post savedPost = target.save(post);

    // when
    savedPost.update("수정된 제목", "수정된 내용");

    // then
    final Post result = target.findById(savedPost.getId()).get();
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo("수정된 제목");
    assertThat(result.getContent()).isEqualTo("수정된 내용");
    assertThat(result.getUsername()).isEqualTo("작성자");
    assertThat(result.getCreatedAt()).isNotNull();
  }

}
