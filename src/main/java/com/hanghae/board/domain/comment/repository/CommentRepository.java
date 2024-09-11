package com.hanghae.board.domain.comment.repository;

import com.hanghae.board.domain.comment.entity.Comment;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Comment> findWithPessimisticLockById(Long id);
}
