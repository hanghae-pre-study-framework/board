package com.hanghae.board.application.controller;

import com.hanghae.board.domain.post.dto.DeletePostCommand;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.post.service.PostWriteService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostReadService postReadService;
  private final PostWriteService postWriteService;

  @GetMapping
  public List<PostDto> getPosts() {
    return postReadService.getPosts();
  }

  @GetMapping("/{id}")
  public PostDto getPost(@PathVariable Long id) {
    return postReadService.getPost(id);
  }


  @PostMapping
  public PostDto create(@RequestBody @Valid PostCommand command) {
    return postWriteService.createPost(command);
  }

  @PutMapping("/{id}")
  public PostDto update(@PathVariable Long id, @RequestBody UpdatePostCommand command) {
    return postWriteService.updatePost(id, command);
  }

  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable Long id, @RequestBody DeletePostCommand command) {
    return postWriteService.deletePost(id, command);
  }
}
