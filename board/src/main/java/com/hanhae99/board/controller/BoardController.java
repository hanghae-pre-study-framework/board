package com.hanhae99.board.controller;

import com.hanhae99.board.domain.Board;
import com.hanhae99.board.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/api/board")
    public Result boardV1() {

        List<Board> findBoards = boardService.findBoards();

        // regId 작성자명으로 바꾸기
        List<BoardDto> collect = findBoards.stream()
                .map(b -> new BoardDto(b.getTitle(),b.getContent(),b.getRegId(),b.getRegDt()))
                .collect(Collectors.toList());
        return new Result(collect.size(),collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class BoardDto {

        private String title;
        private String content;
        private String regName;
        private Timestamp regDate;

    }
}
