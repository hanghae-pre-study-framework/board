package com.hanghae.board.domain.post.repository;

import com.hanghae.board.domain.post.entity.Post;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByOrderByCreatedAtDesc();

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Post> findWithPessimisticLockById(Long id);
}

