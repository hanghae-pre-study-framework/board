package com.hanghae.board.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts", indexes = @Index(name = "idx_posts_created_at", columnList = "created_at"))
@Entity
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 256)
  private String password;

  @Column(nullable = false)
  @ColumnDefault("false")
  private boolean isDestroyed = false;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Builder
  private Post(String title, String content, String username, String password,
      Boolean isDestroyed,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
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