package hh99.BoardProject.registration.controller;

import hh99.BoardProject.registration.entity.BoardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hh99.BoardProject.registration.userService.BoardService;


@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody BoardList list){
        if (boardService.registPost(list) == 0) {
            return ResponseEntity.ok("Your post registered successfully!");
        } else if(boardService.registPost(list) == -10){
            return ResponseEntity.badRequest().body("Username "+list.getUserName()+" does not exists!");
        }
        else if(boardService.registPost(list) == -5){
            return ResponseEntity.badRequest().body("Password is required");
        }
        else{
            return ResponseEntity.badRequest().body("unknown error");
        }
    }
}