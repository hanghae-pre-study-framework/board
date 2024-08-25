package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.entity.BoardList;
import hh99.BoardProject.registration.projection.PostSummaryProjection;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.results.spi.ListResultsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hh99.BoardProject.registration.userService.BoardService;

import java.util.List;


@RestController
@RequiredArgsConstructor //BoardService에 대한 멤버 사용
@RequestMapping("/api/boards")
public class BoardController {


    private final BoardService boardService;
//POST
    //@PostMapping("/register") >> Post의 경우에 자원을 생성하는 행위라 불필요함.
    public ResponseEntity<String> registerUser(@RequestBody BoardList list){
        if (boardService.registPost(list) == 0) {
            return ResponseEntity.ok("Your post registered successfully!");
        } else if(boardService.registPost(list) == -10){
            return ResponseEntity.badRequest().body("Username "+list.getRegId()+" does not exists!");
        }
        else if(boardService.registPost(list) == -5){
            return ResponseEntity.badRequest().body("Password is required");
        }
        else{
            return ResponseEntity.badRequest().body("unknown error");
        }
    }
//GET
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

}