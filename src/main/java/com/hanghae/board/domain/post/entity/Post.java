package com.hanghae.board.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "posts", indexes = @Index(name = "idx_posts_created_at", columnList = "created_at"))
@Entity
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(nullable = false, length = 200)
  private String title;

  @NotNull
  @Column(nullable = false)
  private String content;

  @NotNull
  @Column(nullable = false, length = 50)
  private String username;

  @NotNull
  @Column(nullable = false, length = 256)
  private String password;

  @NotNull
  @Column(nullable = false)
  @ColumnDefault("false")
  private boolean isDestroyed;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column
  private LocalDateTime updatedAt;


  @Builder
  private Post(String title, String content, String username, String password, Boolean isDestroyed,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.title = Objects.requireNonNull(title);
    this.content = Objects.requireNonNull(content);
    this.username = Objects.requireNonNull(username);
    this.password = Objects.requireNonNull(password);
    this.isDestroyed = isDestroyed != null && isDestroyed;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    this.updatedAt = updatedAt;
  }


  public void update(String title, String content, String username, String password,
      LocalDateTime updatedAt) {
    this.title = Objects.requireNonNull(title);
    this.content = Objects.requireNonNull(content);
    this.username = Objects.requireNonNull(username);
    this.password = Objects.requireNonNull(password);
    this.updatedAt = updatedAt == null ? LocalDateTime.now() : updatedAt;
  }

  public void destroy(LocalDateTime updatedAt) {
    this.isDestroyed = true;
    this.updatedAt = updatedAt == null ? LocalDateTime.now() : updatedAt;
  }
}