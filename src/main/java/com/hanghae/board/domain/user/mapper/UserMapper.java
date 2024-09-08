package com.hanghae.board.domain.user.mapper;

import com.hanghae.board.domain.comment.dto.UserDto;
import com.hanghae.board.domain.user.entity.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(User user);

  List<UserDto> toDtos(List<User> users);
}
