package com.hanhae99.board.controller;

import com.hanhae99.board.api.ApiResponse;
import com.hanhae99.board.api.board.BoardAllDTO;
import com.hanhae99.board.api.board.BoardPostDTO;
import com.hanhae99.board.domain.Board;
import com.hanhae99.board.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/api/boards")
    public ResponseEntity<ApiResponse<List<BoardAllDTO>>> boardV1() {
        try{
            List<BoardAllDTO> collect = boardService.getBoardByUserId();
            ApiResponse<List<BoardAllDTO>> response = new ApiResponse<List<BoardAllDTO>>(collect.size(),collect,"success find", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        }catch(Exception e) {
            ApiResponse<List<BoardAllDTO>> response = new ApiResponse<>(0,null,"system error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/api/post")
    public Long save( @RequestBody BoardPostDTO boardPostDTO){
        return boardService.saveBoard(boardPostDTO);
    }


}
