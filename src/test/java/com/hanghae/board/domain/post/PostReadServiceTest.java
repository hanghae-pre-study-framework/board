package com.hanghae.board.domain.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.domain.post.service.PostReadService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostReadServiceTest {

  @Autowired
  private PostReadService postReadService;

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void setUp() {
    postRepository.saveAll(List.of(
        Post.builder().title("Title 1").content("Content 1").username("user1").build(),
        Post.builder().title("Title 2").content("Content 2").username("user2").build(),
        Post.builder().title("Title 3").content("Content 3").username("user3").build()
    ));
  }

  @Test
  void 모든_게시글을_생성일_기준_내림차순으로_조회한다() {
    List<Post> 게시글목록 = postReadService.getPosts();

    assertNotNull(게시글목록);
    assertEquals(3, 게시글목록.size());

    assertTrue(게시글목록.get(0).getCreatedAt().isAfter(게시글목록.get(1).getCreatedAt()));
    assertTrue(게시글목록.get(1).getCreatedAt().isAfter(게시글목록.get(2).getCreatedAt()));

    assertEquals("Title 3", 게시글목록.get(0).getTitle());
    assertEquals("Title 2", 게시글목록.get(1).getTitle());
    assertEquals("Title 1", 게시글목록.get(2).getTitle());
  }
}