package com.hanghae.board.application.usecase;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.exception.PostErrorCode;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.user.exception.UserErrorCode;
import com.hanghae.board.domain.user.service.UserReadService;
import com.hanghae.board.error.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPostUseCaseTest {

  @InjectMocks
  private GetPostUseCase target;
  @Mock
  private PostReadService postReadService;
  @Spy
  private PostMapper postMapper = Mappers.getMapper(PostMapper.class);
  @Mock
  private UserReadService userReadService;

  @Test
  void 게시물조회실패_존재하지않은게시물() {
    // given
    final Long postId = 1L;
    doThrow(new BusinessException(PostErrorCode.POST_NOT_FOUND))
        .when(postReadService).getPost(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.execute(postId));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);
  }

  @Test
  void 게시물조회실패_삭제된게시물() {
    // given
    final Long postId = 1L;

    final PostDto postDto = SUT.giveMeBuilder(PostDto.class)
        .set("id", postId)
        .set("isDestroyed", true)
        .sample();
    doReturn(postDto).when(postReadService).getPost(postId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.execute(postId));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PostErrorCode.POST_DELETED);
  }

  @Test
  void 게시물조회실패_존재하지않은사용자() {
    // given
    final Long postId = 1L;
    final Long userId = 1L;
    final PostDto postDto = SUT.giveMeBuilder(PostDto.class)
        .set("id", postId)
        .set("userId", userId)
        .set("isDestroyed", false)
        .sample();
    doReturn(postDto).when(postReadService).getPost(postId);
    doThrow(new BusinessException(UserErrorCode.USER_NOT_FOUND))
        .when(userReadService).getUser(userId);

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> target.execute(postId));

    // then
    assertThat(result.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
  }

  @Test
  void 게시물조회성공() {
    // given
    final Long postId = 1L;
    final Long userId = 1L;

    final UserDto userDto = SUT.giveMeBuilder(UserDto.class)
        .set("id", userId)
        .sample();
    final PostDto postDto = SUT.giveMeBuilder(PostDto.class)
        .set("id", postId)
        .set("userId", userDto.getId())
        .set("user", userDto)
        .set("isDestroyed", false)
        .sample();

    doReturn(postDto).when(postReadService).getPost(postId);
    doReturn(userDto).when(userReadService).getUser(userId);

    // when
    final PostDto result = target.execute(postId);

    // then
    assertThat(result.getId()).isEqualTo(postId);
    assertThat(result.getTitle()).isEqualTo(postDto.getTitle());
    assertThat(result.getContent()).isEqualTo(postDto.getContent());
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getUser()).isEqualTo(userDto);
    assertThat(result.isDestroyed()).isFalse();
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isEqualTo(postDto.getUpdatedAt());
  }
}