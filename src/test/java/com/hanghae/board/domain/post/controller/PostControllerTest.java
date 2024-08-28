package com.hanghae.board.domain.post.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.hanghae.board.application.controller.PostController;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.service.PostWriteService;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

  @InjectMocks
  private PostController target;

  @Mock
  private PostWriteService postWriteService;

  private MockMvc mockMvc;
  private Gson gson;

  private static Stream<PostCommand> provideInvalidPostCommands() {
    return Stream.of(
        postCommand(null, "content", "username", "password"),
        postCommand("title", null, "username", "password"),
        postCommand("title", "content", null, "password"),
        postCommand("title", "content", "username", null),
        postCommand("", "content", "username", "password"),
        postCommand("title", "", "username", "password"),
        postCommand("title", "content", "", "password"),
        postCommand("title", "content", "username", "")
    );
  }

  private static PostCommand postCommand(String title, String content, String username,
      String password) {
    return PostCommand.builder()
        .title(title)
        .content(content)
        .username(username)
        .password(password)
        .build();
  }

  @BeforeEach
  void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(target).build();
    gson = new Gson();
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPostCommands")
  void 게시글작성실패_필수값없음(PostCommand invalidPostCommand) throws Exception {
    // given
    final String url = "/posts";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(invalidPostCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  void 게시글작성성공() throws Exception {
    // given
    final String url = "/posts";
    final String title = "title";
    final String content = "content";
    final String username = "username";
    final String password = "password";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(postCommand(title, content, username, password)))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk());
  }
}