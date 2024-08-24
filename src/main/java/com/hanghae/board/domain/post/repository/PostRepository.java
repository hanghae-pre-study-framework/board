package com.hanghae.board.domain.post.repository;

import com.hanghae.board.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByOrderByCreatedAtDesc();
}

