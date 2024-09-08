package com.hanghae.board.domain.post.mapper;

import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(source = "destroyed", target = "isDestroyed")
  @Mapping(target = "user", ignore = true)
  PostDto toDto(Post post);

  @Mapping(source = "post.id", target = "id")
  @Mapping(source = "post.destroyed", target = "isDestroyed")
  @Mapping(source = "userDto", target = "user")
  PostDto toDto(PostDto post, UserDto userDto);
}