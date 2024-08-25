package com.hanghae.board.application;

import com.hanghae.board.domain.post.dto.PostCommand;
import com.hanghae.board.domain.post.dto.PostDto;
import com.hanghae.board.domain.post.service.PostReadService;
import com.hanghae.board.domain.post.service.PostWriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
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
  public PostDto create(@RequestBody PostCommand command) {
    return postWriteService.create(command);
  }
}
