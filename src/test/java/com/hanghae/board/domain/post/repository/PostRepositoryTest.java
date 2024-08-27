package com.hanghae.board.domain.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanghae.board.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Test
  void 게시글_등록() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .password("비밀번호")
        .build();

    // when
    final Post result = postRepository.save(post);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo("제목");
    assertThat(result.getContent()).isEqualTo("내용");
    assertThat(result.getUsername()).isEqualTo("작성자");
    assertThat(result.getPassword()).isEqualTo("비밀번호");
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
        .password("비밀번호")
        .build();
    final Post savedPost = postRepository.save(post);

    // when
    LocalDateTime now = LocalDateTime.now();
    savedPost.update("수정된 제목", "수정된 내용", "작성자", "비밀번호", now);

    // then
    final Post result = postRepository.findById(savedPost.getId()).get();
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo("수정된 제목");
    assertThat(result.getContent()).isEqualTo("수정된 내용");
    assertThat(result.getUsername()).isEqualTo("작성자");
    assertThat(result.getPassword()).isEqualTo("비밀번호");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isEqualTo(now);
  }

  @Test
  void 게시글_삭제() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .password("비밀번호")
        .build();
    final Post savedPost = postRepository.save(post);

    // when
    postRepository.delete(savedPost);

    // then
    assertThat(postRepository.findById(savedPost.getId())).isEmpty();
  }

  @Test
  void 게시글_조회() {
    // given
    final Post post = Post.builder()
        .title("제목")
        .content("내용")
        .username("작성자")
        .password("비밀번호")
        .build();
    final Post savedPost = postRepository.save(post);

    // when
    final Post result = postRepository.findById(savedPost.getId()).get();

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getTitle()).isEqualTo("제목");
    assertThat(result.getContent()).isEqualTo("내용");
    assertThat(result.getUsername()).isEqualTo("작성자");
    assertThat(result.getPassword()).isEqualTo("비밀번호");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNull();
  }

  @Test
  void 게시글_전체_조회_내림차순() {
    // given
    final Post post1 = Post.builder()
        .title("제목1")
        .content("내용1")
        .username("작성자1")
        .password("비밀번호1")
        .build();
    final Post post2 = Post.builder()
        .title("제목2")
        .content("내용2")
        .username("작성자2")
        .password("비밀번호2")
        .build();
    final Post post3 = Post.builder()
        .title("제목3")
        .content("내용3")
        .username("작성자3")
        .password("비밀번호3")
        .build();
    postRepository.save(post1);
    postRepository.save(post2);
    postRepository.save(post3);

    // when
    final List<Post> result = postRepository.findAllByIsDestroyedOrderByCreatedAtDesc(false);

    // then
    assertThat(result).hasSize(3);
    assertThat(result).extracting("title")
        .containsExactly("제목3", "제목2", "제목1");
  }
}
