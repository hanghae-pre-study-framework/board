package com.hanghae.board.application.controller;


import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.board.application.usecase.AddCommentUseCase;
import com.hanghae.board.config.SecurityConfig;
import com.hanghae.board.domain.comment.dto.CommentCommand;
import com.hanghae.board.error.GlobalExceptionHandler;
import com.hanghae.board.security.UserDetailsServiceImpl;
import com.hanghae.board.security.jwt.JwtTokenProvider;
import com.hanghae.board.util.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CommentController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class CommentControllerTest {

  private static final String BASE_URL = "/posts/{postId}/comments";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AddCommentUseCase addCommentUseCase;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private UserDetailsServiceImpl userDetailsService;

  @Test
  @WithMockCustomUser(username = "username")
  void 댓글작성_성공() throws Exception {
    // given
    final String url = BASE_URL;
    final Long postId = 1L;
    final CommentCommand command = SUT.giveMeOne(CommentCommand.class);

    // when
    final ResultActions result = mockMvc.perform(post(url, postId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(command))
    );

    // then
    result.andExpect(status().isCreated());
  }
}
