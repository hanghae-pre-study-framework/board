package com.hanghae.board.domain.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.error.BusinessException;
import com.hanghae.board.utill.PostTestFixture;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
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

  private EasyRandom postFixture;

  @BeforeEach
  void setUp() {
    postRepository.deleteAll();

    LocalDate now = LocalDate.now();
    LocalDate oneYearAgo = now.minusYears(1);
    postFixture = PostTestFixture.get(oneYearAgo, now);
  }

  @AfterEach
  void tearDown() {
    postRepository.deleteAll();
  }

  @Test
  void 모든_게시글을_생성일_기준_내림차순으로_조회한다() {
    IntStream.range(0, 5)
        .mapToObj(i -> postFixture.nextObject(Post.class))
        .forEach(postRepository::save);

    List<PostDto> 게시글목록 = postReadService.getPosts();

    assertNotNull(게시글목록);
    assertEquals(5, 게시글목록.size());

    // 생성일 기준 내림차순 정렬 확인
    게시글목록.stream()
        .reduce((a, b) -> {
          assertTrue(a.createdAt().isAfter(b.createdAt()));
          return b;
        });
  }

  @Test
  void 게시글_단건을_조회한다() {
    Post 저장된게시글 = postRepository.save(postFixture.nextObject(Post.class));

    PostDto 조회된게시글 = postReadService.getPost(저장된게시글.getId());

    assertNotNull(조회된게시글);
    assertEquals(저장된게시글.getId(), 조회된게시글.id());
    assertEquals(저장된게시글.getTitle(), 조회된게시글.title());
    assertEquals(저장된게시글.getContent(), 조회된게시글.content());
    assertEquals(저장된게시글.getUsername(), 조회된게시글.username());
  }

  @Test
  void 존재하지_않는_게시글_조회시_예외발생() {
    long 존재하지_않는_ID = -1;

    BusinessException exception = assertThrows(BusinessException.class,
        () -> postReadService.getPost(존재하지_않는_ID));

    assertEquals(PostErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    assertEquals(PostErrorCode.POST_NOT_FOUND.getStatus(), exception.getErrorCode().getStatus());
    assertEquals(PostErrorCode.POST_NOT_FOUND.getMessage(), exception.getMessage());
  }
}