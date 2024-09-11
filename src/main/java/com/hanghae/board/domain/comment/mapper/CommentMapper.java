package com.hanghae.board.domain.comment.mapper;

import com.hanghae.board.domain.comment.dto.CommentDetailDto;
import com.hanghae.board.domain.comment.dto.CommentDto;
import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  CommentDto toDto(Comment comment);

  @Mapping(source = "comment.id", target = "id")
  @Mapping(source = "userDto", target = "user")
  CommentDetailDto toCommentDetailDto(CommentDto comment, UserDto userDto);
}