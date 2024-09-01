package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.error.ErrorCodes;
import hh99.BoardProject.registration.projection.PostSummaryProjection;
import hh99.BoardProject.registration.userService.BoardDeleteService;
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
@RequestMapping("/api/posts")
public class BoardController {


    private final BoardWriteService boardWriteService;
    private final BoardService boardService;
    private final BoardDeleteService boardDeleteService;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> registOnePost(@RequestBody BoardList list){
        if (boardWriteService.registPost(list).getSeqNo() > 0) {
            System.out.println("Your Post Number is "+ list.getSeqNo());
            return ResponseEntity.status(HttpStatus.CREATED).body(list);
        } else if(boardWriteService.registPost(list).getRegId() == ""){ //등록자 찾지못함
            //return (ResponseEntity<BoardList>) ResponseEntity.status(-10);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodes.USER_NOT_FOUND);
        }
        else if(boardWriteService.registPost(list).getPassword() == ""){ //비밀번호 누락
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodes.MISSING_PASSWORD);
        }
        else{
            return(ResponseEntity<BoardList>) ResponseEntity.badRequest();
        }
    }

    //게시판 리스트 조회
    @GetMapping(value = "/all")
    public ResponseEntity<List<BoardList>> getAllPosts() {
        List<BoardList> posts = boardService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/all2")
    public ResponseEntity<List<BoardList>> getAllPosts2() {
        List<BoardList> posts = boardService.getAllPosts2();
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value="/all3")
    public ResponseEntity<List<PostSummaryProjection>> getAllPosts3() {
        List<PostSummaryProjection> posts = boardService.getAllPost3();
        return ResponseEntity.ok(posts);
    }

    //게시물 단건 조회
    @GetMapping(value = "/post/{seq_no}")
    public ResponseEntity<?> getPost(@PathVariable("seq_no") Integer seq_no){
        BoardList post = boardService.getPost(seq_no);

        if(post != null)
            return ResponseEntity.ok(post);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodes.POST_NOT_FOUND);
    }


    //게시글 수정
    @PutMapping("/post/{seq_no}")
    public ResponseEntity<?> updatePost(@PathVariable("seq_no") Integer seq_no, @RequestBody BoardList list){
        BoardList editedPost = boardWriteService.editPost(list, seq_no);

        if(editedPost != null)
            return ResponseEntity.ok(editedPost);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodes.PASSWORD_NOT_CORRECT);
    }

    //게시글 삭제
    @PostMapping("/post/{seq_no}/delete")
    public String deletePost(@PathVariable("seq_no")Integer seq_no, @RequestBody BoardList list){
       return boardDeleteService.DeletePost(list, seq_no) ? "The Post is deleted Successfully" : "This Post can't delete";
    }
}