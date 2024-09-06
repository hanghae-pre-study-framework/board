package com.hanhae99.board.service;

import com.hanhae99.board.api.board.BoardAllDTO;
import com.hanhae99.board.api.board.BoardPostDTO;
import com.hanhae99.board.domain.Board;
import com.hanhae99.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardAllDTO> getBoardByUserId() {

        List<BoardAllDTO> findBoards = boardRepository.findBoardByUserId();

        return findBoards.stream()
                .map(b -> new BoardAllDTO(b.getTitle(),b.getRegName(),b.getContent(),b.getRegDate()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long saveBoard(BoardPostDTO requestDto){

        return boardRepository.save(requestDto.toEntity()).getBoardId();
    }

}
