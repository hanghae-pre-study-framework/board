package com.hanghae.board.domain.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hanghae.board.domain.post.dto.DeletePostCommand;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.repository.PostRepository;
import com.hanghae.board.domain.post.service.PostWriteService;
import com.hanghae.board.error.BusinessException;
import com.hanghae.board.utill.PostTestFixture;
import java.time.LocalDate;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostWriteServiceTest {

  @Autowired
  private PostWriteService postWriteService;

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
  void 게시물_단건_생성() {
    PostCommand postCommand = PostTestFixture.nextPostCommand(postFixture);

    var post = postWriteService.createPost(postCommand);

    assertThat(post.id()).isNotNull();
    assertThat(post.title()).isEqualTo(postCommand.title());
    assertThat(post.content()).isEqualTo(postCommand.content());
    assertThat(post.username()).isEqualTo(postCommand.username());
  }

  @ParameterizedTest
  @NullSource
  void 게시물_생성_Null_체크(String nullValue) {
    assertThrows(NullPointerException.class, () -> {
      PostCommand postCommand = new PostCommand(nullValue, "content", "username", "password");
      postWriteService.createPost(postCommand);
    });

    assertThrows(NullPointerException.class, () -> {
      PostCommand postCommand = new PostCommand("title", nullValue, "username", "password");
      postWriteService.createPost(postCommand);
    });

    assertThrows(NullPointerException.class, () -> {
      PostCommand postCommand = new PostCommand("title", "content", nullValue, "password");
      postWriteService.createPost(postCommand);
    });
  }

  @Test
  void 게시물_단건_수정_성공() {
    PostCommand postCommand = PostTestFixture.nextPostCommand(postFixture);
    var post = postWriteService.createPost(postCommand);

    UpdatePostCommand updatePostCommand = PostTestFixture.nextUpdatePostCommand(postFixture);
    UpdatePostCommand updatePostCommandWithPassword = new UpdatePostCommand(
        updatePostCommand.title(),
        updatePostCommand.content(),
        updatePostCommand.username(),
        postCommand.password()
    );

    var updatedPost = postWriteService.updatePost(post.id(), updatePostCommandWithPassword);

    assertThat(updatedPost.id()).isEqualTo(post.id());
    assertThat(updatedPost.title()).isEqualTo(updatePostCommand.title());
    assertThat(updatedPost.content()).isEqualTo(updatePostCommand.content());
    assertThat(updatedPost.username()).isEqualTo(updatePostCommand.username());
  }

  @ParameterizedTest
  @NullSource
  void 게시물_수정_Null_체크(String nullValue) {
    PostCommand postCommand = PostTestFixture.nextPostCommand(postFixture);
    var post = postWriteService.createPost(postCommand);

    assertThrows(NullPointerException.class, () -> {
      UpdatePostCommand updateCommand = new UpdatePostCommand(nullValue, "content", "username",
          postCommand.password());
      postWriteService.updatePost(post.id(), updateCommand);
    });

    assertThrows(NullPointerException.class, () -> {
      UpdatePostCommand updateCommand = new UpdatePostCommand("title", nullValue, "username",
          postCommand.password());
      postWriteService.updatePost(post.id(), updateCommand);
    });

    assertThrows(NullPointerException.class, () -> {
      UpdatePostCommand updateCommand = new UpdatePostCommand("title", "content", nullValue,
          postCommand.password());
      postWriteService.updatePost(post.id(), updateCommand);
    });
  }

  @Test
  void 게시물_수정시_비밀번호_불일치() {
    PostCommand postCommand = PostTestFixture.nextPostCommand(postFixture);
    var post = postWriteService.createPost(postCommand);

    UpdatePostCommand updatePostCommand = PostTestFixture.nextUpdatePostCommand(postFixture);

    BusinessException exception = assertThrows(BusinessException.class,
        () -> postWriteService.updatePost(post.id(), updatePostCommand));

    assertEquals(PostErrorCode.POST_PASSWORD_MISMATCH, exception.getErrorCode());
    assertEquals(PostErrorCode.POST_PASSWORD_MISMATCH.getStatus(),
        exception.getErrorCode().getStatus());
    assertEquals(PostErrorCode.POST_PASSWORD_MISMATCH.getMessage(), exception.getMessage());
  }

  @Test
  void 존재하지_않는_게시물_수정_시도() {
    UpdatePostCommand updatePostCommand = PostTestFixture.nextUpdatePostCommand(postFixture);
    long 존재하지_않는_ID = -1;

    BusinessException exception = assertThrows(BusinessException.class,
        () -> postWriteService.updatePost(존재하지_않는_ID, updatePostCommand));

    assertEquals(PostErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    assertEquals(PostErrorCode.POST_NOT_FOUND.getStatus(), exception.getErrorCode().getStatus());
    assertEquals(PostErrorCode.POST_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void 게시물_단건_삭제_성공() {
    PostCommand postCommand = PostTestFixture.nextPostCommand(postFixture);
    var post = postWriteService.createPost(postCommand);

    DeletePostCommand deletePostCommand = new DeletePostCommand(postCommand.password());
    boolean isDeleted = postWriteService.deletePost(post.id(), deletePostCommand);

    assertThat(isDeleted).isTrue();
  }

  @Test
  void 게시물_삭제시_비밀번호_불일치() {
    PostCommand postCommand = PostTestFixture.nextPostCommand(postFixture);
    var post = postWriteService.createPost(postCommand);

    DeletePostCommand deletePostCommand = new DeletePostCommand("wrong_password");
    BusinessException exception = assertThrows(BusinessException.class,
        () -> postWriteService.deletePost(post.id(), deletePostCommand));

    assertEquals(PostErrorCode.POST_PASSWORD_MISMATCH, exception.getErrorCode());
    assertEquals(PostErrorCode.POST_PASSWORD_MISMATCH.getStatus(),
        exception.getErrorCode().getStatus());
    assertEquals(PostErrorCode.POST_PASSWORD_MISMATCH.getMessage(), exception.getMessage());
  }

  @Test
  void 존재하지_않는_게시물_삭제_시도() {
    long 존재하지_않는_ID = -1;
    DeletePostCommand deletePostCommand = new DeletePostCommand("password");

    BusinessException exception = assertThrows(BusinessException.class,
        () -> postWriteService.deletePost(존재하지_않는_ID, deletePostCommand));

    assertEquals(PostErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    assertEquals(PostErrorCode.POST_NOT_FOUND.getStatus(), exception.getErrorCode().getStatus());
    assertEquals(PostErrorCode.POST_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void 이미_삭제된_게시물_삭제_시도() {
    PostCommand postCommand = PostTestFixture.nextPostCommand(postFixture);
    var post = postWriteService.createPost(postCommand);

    DeletePostCommand deletePostCommand = new DeletePostCommand(postCommand.password());
    postWriteService.deletePost(post.id(), deletePostCommand);

    BusinessException exception = assertThrows(BusinessException.class,
        () -> postWriteService.deletePost(post.id(), deletePostCommand));

    assertEquals(PostErrorCode.POST_ALREADY_DELETED, exception.getErrorCode());
    assertEquals(PostErrorCode.POST_ALREADY_DELETED.getStatus(),
        exception.getErrorCode().getStatus());
    assertEquals(PostErrorCode.POST_ALREADY_DELETED.getMessage(), exception.getMessage());
  }
}