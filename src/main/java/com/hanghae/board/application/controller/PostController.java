package com.hanghae.board.application.controller;

import com.hanghae.board.domain.post.dto.DeletePostCommand;
import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.dto.UpdatePostCommand;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.post.service.PostWriteService;
import com.hanghae.board.security.CurrentUser;
import com.hanghae.board.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @GetMapping("/{id:\\d+}")
  public ResponseEntity<PostDto> getPost(@PathVariable Long id) {
    PostDto post = postReadService.getPost(id);

    return ResponseEntity.status(HttpStatus.OK).body(post);
  }


  @PostMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<PostDto> create(@RequestBody @Valid PostCommand command,
      @CurrentUser UserPrincipal currentUser) {
    PostDto post = postWriteService.createPost(command, currentUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(post);
  }

  @PutMapping("/{id:\\d+}")
  public ResponseEntity<PostDto> update(@PathVariable Long id,
      @RequestBody UpdatePostCommand command) {
    PostDto post = postWriteService.updatePost(id, command);

    return ResponseEntity.status(HttpStatus.OK).body(post);
  }

  @DeleteMapping("/{id:\\d+}")
  public ResponseEntity<Boolean> delete(@PathVariable Long id,
      @RequestBody DeletePostCommand command) {
    Boolean result = postWriteService.deletePost(id, command);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
