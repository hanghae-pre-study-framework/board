package com.hanghae.board.domain.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanghae.board.application.controller.PostController;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.post.service.PostWriteService;
import com.hanghae.board.error.GlobalExceptionHandler;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

  @InjectMocks
  private PostController target;

  @Mock
  private PostReadService postReadService;

  @Mock
  private PostWriteService postWriteService;


  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

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
    mockMvc = MockMvcBuilders.standaloneSetup(target)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
    objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
  }

  @Test
  void 게시글목록조회성공_생성일내림차순() throws Exception {
    // given
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime earlier = now.minusHours(1);
    List<PostDto> postList = Arrays.asList(
        new PostDto(2L, "Title 2", "Content 2", "author2", now, null),
        new PostDto(1L, "Title 1", "Content 1", "author1", earlier, null)
    );
    doReturn(postList).when(postReadService).getPosts();

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get("/posts")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    List<PostDto> responses = objectMapper.readValue(jsonResponse,
        new TypeReference<List<PostDto>>() {
        });

    assertThat(responses).hasSize(2);
    assertThat(responses.get(0).id()).isEqualTo(2L);
    assertThat(responses.get(0).createdAt()).isAfter(responses.get(1).createdAt());

    verify(postReadService).getPosts();
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPostCommands")
  void 게시글작성실패_필수값없음(PostCommand invalidPostCommand) throws Exception {
    // given
    final String url = "/posts";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(objectMapper.writeValueAsString(invalidPostCommand))
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
            .content(objectMapper.writeValueAsString(PostCommand.builder()
                .title(title)
                .content(content)
                .username(username)
                .password(password)
                .build()))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk());
  }
}