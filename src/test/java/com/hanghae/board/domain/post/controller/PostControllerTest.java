package com.hanghae.board.domain.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanghae.board.application.controller.PostController;
import com.hanghae.board.domain.post.dto.DeletePostCommand;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.post.service.PostWriteService;
import com.hanghae.board.error.BusinessException;
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

  private static UpdatePostCommand updatePostCommand(String title, String content, String username,
      String password) {
    return UpdatePostCommand.builder()
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
  void 게시글조회_실패_존재하지않는게시글() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long nonExistentPostId = -1L;
    doThrow(new BusinessException(PostErrorCode.POST_NOT_FOUND)).when(postReadService)
        .getPost(nonExistentPostId);

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get(url, nonExistentPostId).contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isNotFound());

    // verify
    verify(postReadService).getPost(nonExistentPostId);
  }

  @Test
  void 단일게시글조회_성공() throws Exception {
    // given
    Long postId = 1L;
    PostDto postDto = new PostDto(postId, "Title", "Content", "author", LocalDateTime.now(), null);
    doReturn(postDto).when(postReadService).getPost(postId);

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.get("/posts/{id}", postId)
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk());

    // verify
    verify(postReadService).getPost(postId);
  }

  @Test
  void 게시글목록조회_성공_생성일내림차순() throws Exception {
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
  void 게시글작성_실패_필수값없음(PostCommand invalidPostCommand) throws Exception {
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
  void 게시글작성_성공() throws Exception {
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

  @Test
  void 게시글수정_실패_존재하지않는게시글() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long nonExistentPostId = -1L;
    UpdatePostCommand updatePostCommand = updatePostCommand("title", "content", "username",
        "password");
    doThrow(new BusinessException(PostErrorCode.POST_NOT_FOUND))
        .when(postWriteService).updatePost(eq(nonExistentPostId), any(UpdatePostCommand.class));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.put(url, nonExistentPostId)
            .content(objectMapper.writeValueAsString(updatePostCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isNotFound());

    // verify
    verify(postWriteService).updatePost(eq(nonExistentPostId), any(UpdatePostCommand.class));
  }

  @Test
  void 게시글수정_실패_비밀번호불일치() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long postId = 1L;
    UpdatePostCommand updateCommand = updatePostCommand("title", "content", "username", "password");
    doThrow(new BusinessException(PostErrorCode.POST_PASSWORD_MISMATCH))
        .when(postWriteService).updatePost(eq(postId), any(UpdatePostCommand.class));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.put(url, postId)
            .content(objectMapper.writeValueAsString(updateCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isForbidden());

    // verify
    verify(postWriteService).updatePost(eq(postId), any(UpdatePostCommand.class));
  }

  @Test
  void 게시글수정_실패_게시글삭제됨() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long postId = 1L;
    UpdatePostCommand updatePostCommand = updatePostCommand("title", "content", "username",
        "password");
    doThrow(new BusinessException(PostErrorCode.POST_ALREADY_DELETED))
        .when(postWriteService).updatePost(eq(postId), any(UpdatePostCommand.class));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.put(url, postId)
            .content(objectMapper.writeValueAsString(updatePostCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest());

    // verify
    verify(postWriteService).updatePost(eq(postId), any(UpdatePostCommand.class));
  }

  @Test
  void 게시글수정_성공() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long postId = 1L;
    UpdatePostCommand updatePostCommand = updatePostCommand("title", "content", "username",
        "password");

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.put(url, postId)
            .content(objectMapper.writeValueAsString(updatePostCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk());

    // verify
    verify(postWriteService).updatePost(eq(postId), any(UpdatePostCommand.class));
  }

  @Test
  void 게시글삭제_실패_존재하지않는게시글() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long nonExistentPostId = -1L;
    DeletePostCommand deletePostCommand = new DeletePostCommand("password");
    doThrow(new BusinessException(PostErrorCode.POST_NOT_FOUND)).when(postWriteService)
        .deletePost(eq(nonExistentPostId), any(DeletePostCommand.class));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.delete(url, nonExistentPostId)
            .content(objectMapper.writeValueAsString(deletePostCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isNotFound());

    // verify
    verify(postWriteService).deletePost(eq(nonExistentPostId), any(DeletePostCommand.class));
  }

  @Test
  void 게시글삭제_실패_비밀번호불일치() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long postId = 1L;
    DeletePostCommand deletePostCommand = new DeletePostCommand("password");
    doThrow(new BusinessException(PostErrorCode.POST_PASSWORD_MISMATCH)).when(postWriteService)
        .deletePost(eq(postId), any(DeletePostCommand.class));

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.delete(url, postId)
            .content(objectMapper.writeValueAsString(deletePostCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isForbidden());

    // verify
    verify(postWriteService).deletePost(eq(postId), any(DeletePostCommand.class));
  }

  @Test
  void 게시글삭제_성공() throws Exception {
    // given
    final String url = "/posts/{id}";
    Long postId = 1L;
    DeletePostCommand deletePostCommand = new DeletePostCommand("password");

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.delete(url, postId)
            .content(objectMapper.writeValueAsString(deletePostCommand))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk());

    // verify
    verify(postWriteService).deletePost(eq(postId), any(DeletePostCommand.class));
  }

}