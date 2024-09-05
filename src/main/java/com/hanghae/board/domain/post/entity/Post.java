package com.hanghae.board.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "posts", indexes = @Index(name = "idx_posts_created_at", columnList = "created_at"))
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "제목은 비어있을 수 없습니다")
  @Column(nullable = false, length = 200)
  private String title;

  @NotEmpty(message = "내용은 비어있을 수 없습니다")
  @Column(nullable = false)
  private String content;

  @NotEmpty(message = "사용자 이름은 비어있을 수 없습니다")
  @Column(nullable = false, length = 50)
  private String username;

  @Builder.Default
  @Column(nullable = false)
  private boolean isDestroyed = false;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(insertable = false)
  private LocalDateTime updatedAt;

  public void update(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public void destroy() {
    this.isDestroyed = true;
  }
}