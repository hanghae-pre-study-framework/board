package com.hanghae.board.domain.post.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.hanghae.board.application.controller.PostController;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.service.PostWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
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

  @BeforeEach
  void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(target).build();
    gson = new Gson();
  }

  @NullSource
  @NullAndEmptySource
  void 게시글작성실패_필수값없음(String emptyValue) throws Exception {
    // given
    final String url = "/posts";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(postCommand(emptyValue, emptyValue, emptyValue, emptyValue)))
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

  private PostCommand postCommand(String title, String content, String username, String password) {
    return PostCommand.builder()
        .title(title)
        .content(content)
        .username(username)
        .password(password)
        .build();
  }

}
