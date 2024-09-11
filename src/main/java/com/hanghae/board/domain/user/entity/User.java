package com.hanghae.board.domain.user.entity;

import com.hanghae.board.domain.user.dto.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Positive
  private Long id;

  @Column(nullable = false, unique = true, length = 10)
  @NotNull
  @Size(max = 10, message = "10자 이하로 입력해주세요.")
  private String username;

  @Column(nullable = false, length = 60)
  @NotNull
  @Size(max = 60)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  @Builder.Default
  private UserRole role = UserRole.USER;
}
