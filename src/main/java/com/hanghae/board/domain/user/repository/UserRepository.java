package com.hanghae.board.domain.user.repository;

import com.hanghae.board.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsername(final String username);

  Optional<User> findByUsername(final String username);

}
