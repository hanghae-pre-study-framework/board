package com.hanghae.board.domain.user.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanghae.board.domain.user.application.controller.UserController;
import com.hanghae.board.domain.user.dto.UserCommand;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.service.UserWriteService;
import com.hanghae.board.error.BusinessException;
import com.hanghae.board.error.GlobalExceptionHandler;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @InjectMocks
  private UserController target;

  @Mock
  private UserWriteService userWriteService;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  private static Stream<UserCommand> provideInvalidUserCommands() {
    return Stream.of(
        userCommand(null, "validPass1"),
        userCommand("user1", null),
        userCommand("", "validPass1"),
        userCommand("user1", ""),
        userCommand(" ", "validPass1"),
        userCommand("user1", " "),
        userCommand("abc", "validPass1"),  // username 최소 4자 이상
        userCommand("verylonguser", "validPass1"),  // username 최대 10자 이하
        userCommand("user!", "validPass1"),  // username 알파벳 소문자(a~z), 숫자(0~9)만 허용
        userCommand("user1", "short12"),  // password 최소 8자 이상
        userCommand("user1", "verylongpassword12345"),  // password 최대 20자 이하
        userCommand("user1", "validpass!")  // password 알파벳 대문자(A~Z), 소문자(a~z), 숫자(0~9)만 허용
    );
  }

  private static UserCommand userCommand(String username, String password) {
    return UserCommand.builder()
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

  @ParameterizedTest
  @MethodSource("provideInvalidUserCommands")
  void 유저등록_실패_유효성검사(UserCommand command) throws Exception {
    // given
    final String url = "/users";

    // when
    final ResultActions resultActions = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(command)));

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  void 유저등록_실패_유저네임중복() throws Exception {
    // given
    final String url = "/users";
    final UserCommand command = UserCommand.builder()
        .username("test")
        .password("password")
        .build();
    doThrow(new BusinessException(UserErrorCode.USERNAME_DUPLICATION))
        .when(userWriteService).createUser(any(UserCommand.class));

    // when
    final ResultActions resultActions = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(command)));

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  void 유저등록_성공() throws Exception {
    // given
    final String url = "/users";
    final UserCommand command = UserCommand.builder()
        .username("Test123")
        .password("Password1234")
        .build();

    // when
    final ResultActions resultActions = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(command)));

    // then
    resultActions.andExpect(status().isOk());
  }
}
