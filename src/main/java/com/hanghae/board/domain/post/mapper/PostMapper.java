package com.hanghae.board.domain.post.mapper;

import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

  PostDto toDto(Post post);
}