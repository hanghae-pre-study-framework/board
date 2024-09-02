package com.hanghae.board.domain.user.repository;

import com.hanghae.board.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsername(final String username);

}
