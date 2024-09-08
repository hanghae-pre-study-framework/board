package com.hanghae.board.application.usecase;

import static com.hanghae.board.util.FixtureCommon.SUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.mapper.PostMapper;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.user.service.UserReadService;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPostsUseCaseTest {

  @Spy
  private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
  @InjectMocks
  private GetPostsUseCase target;
  @Mock
  private PostReadService postReadService;
  @Mock
  private UserReadService userReadService;

  @Test
  void 게시물목록조회_성공() {
    // given
    final Random random = new Random();
    final List<UserDto> users = SUT.giveMe(UserDto.class, 5);
    final List<PostDto> posts = SUT.giveMeBuilder(PostDto.class)
        .setLazy("user", () -> users.get(random.nextInt(5)))
        .thenApply((it, builder) -> builder.set("userId", it.getUser().getId()))
        .sampleList(10);
    final List<Long> userIds = posts.stream().map(PostDto::getUserId).toList();

    doReturn(posts).when(postReadService).getPosts();
    doReturn(users).when(userReadService).getUsers(userIds);

    // when
    final List<PostDto> result = target.execute();

    // then
    assertThat(result).hasSize(10);
    for (PostDto post : result) {
      assertThat(users).contains(post.getUser());
      assertThat(post.getUserId()).isEqualTo(post.getUser().getId());
    }
  }
}
