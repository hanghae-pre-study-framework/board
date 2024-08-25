package com.hanghae.board.domain.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.utill.PostTestFixture;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostReadServiceTest {

  @Autowired
  private PostReadService postReadService;

  @Autowired
  private PostRepository postRepository;

  private List<Post> testPosts;

  @BeforeEach
  void setUp() {
    var easyRandom = PostTestFixture.get(
        LocalDate.of(1970, 1, 1),
        LocalDate.of(2024, 8, 20)
    );

    testPosts = IntStream.range(0, 5)
        .parallel()
        .mapToObj(i -> easyRandom.nextObject(Post.class))
        .toList();

    postRepository.saveAll(testPosts);
  }

  @Test
  void 모든_게시글을_생성일_기준_내림차순으로_조회한다() {
    var 게시글목록 = postReadService.getPosts();

    assertNotNull(게시글목록);
    assertEquals(testPosts.size(), 게시글목록.size());

    for (int i = 0; i < 게시글목록.size() - 1; i++) {
      assertTrue(게시글목록.get(i).createdAt().isAfter(게시글목록.get(i + 1).createdAt()));
    }
  }
}