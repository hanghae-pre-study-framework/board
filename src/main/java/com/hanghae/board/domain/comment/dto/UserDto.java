package com.hanghae.board.domain.comment.dto;

import com.hanghae.board.domain.user.dto.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDto {

  @Positive
  @NotNull
  private final Long id;

  @NotEmpty
  private final String username;

  @NotNull
  private final UserRole role;

}
