package com.hanghae.board.domain.post;

import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.service.PostWriteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class PostWriteServiceTest {

  @Autowired
  private PostWriteService postWriteService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void 게시물_단건_생성() {
    PostCommand postCommand = new PostCommand(
        "제목",
        "내용",
        "작성자",
        "비밀번호"
    );

    var post = postWriteService.create(postCommand);

    Assertions.assertThat(post.id()).isNotNull();
    Assertions.assertThat(post.title()).isEqualTo(postCommand.title());
    Assertions.assertThat(post.content()).isEqualTo(postCommand.content());
    Assertions.assertThat(post.username()).isEqualTo(postCommand.username());
    Assertions.assertThat(passwordEncoder.matches(postCommand.password(), post.password()))
        .isTrue();

  }
}