package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.projection.PostSummaryProjection;
import hh99.BoardProject.registration.userService.BoardWriteService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.sql.results.spi.ListResultsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import hh99.BoardProject.registration.userService.BoardService;

import java.util.List;



@RestController
@RequiredArgsConstructor //BoardService에 대한 멤버 사용
@RequestMapping("/api/boards")
public class BoardController {


    private final BoardWriteService boardWriteService;
    private final BoardService boardService;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BoardList> registOnePost(@RequestBody BoardList list){
        if (boardWriteService.registPost(list).getSeqNo() > 0) {
            System.out.println("Your Post Number is "+ list.getSeqNo());
            return ResponseEntity.status(HttpStatus.CREATED).body(list);
        } else if(boardWriteService.registPost(list).getRegId() == ""){ //등록자 누락
            return (ResponseEntity<BoardList>) ResponseEntity.status(-10);
        }
        else if(boardWriteService.registPost(list).getPassword() == ""){ //비밀번호 누락
            return (ResponseEntity<BoardList>) ResponseEntity.status(-11);
        }
        else{
            return(ResponseEntity<BoardList>) ResponseEntity.badRequest();
        }
    }

    //게시판 리스트 조회
    @GetMapping(value = "/posts")
    public ResponseEntity<List<BoardList>> getAllPosts() {
        List<BoardList> posts = boardService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/posts2")
    public ResponseEntity<List<BoardList>> getAllPosts2() {
        List<BoardList> posts = boardService.getAllPosts2();
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value="/posts3")
    public ResponseEntity<List<PostSummaryProjection>> getAllPosts3() {
        List<PostSummaryProjection> posts = boardService.getAllPost3();
        return ResponseEntity.ok(posts);
    }

    //게시물 단건 조회
    @GetMapping(value = "/posts/{seq_no}")
    public ResponseEntity<BoardList> getPost(@PathVariable("seq_no") Integer seq_no){
        BoardList post = boardService.getPost(seq_no);
        return ResponseEntity.ok(post);
    }


    //게시글 수정
    @PostMapping("/posts/{seq_no}/edit")
    public ResponseEntity<BoardList> updatePost(@PathVariable("seq_no") Integer seq_no, @RequestBody BoardList list){
        BoardList post = boardService.getPost(seq_no);
        String password = post.getPassword();

        if(list.getPassword().equals(password))
            return ResponseEntity.ok(boardWriteService.EditPost(list, seq_no));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}