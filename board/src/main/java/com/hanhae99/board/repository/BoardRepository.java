package com.hanhae99.board.repository;

import com.hanhae99.board.domain.Board;
import com.hanhae99.board.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    public List<Board> findAll(){
        List<Board> result = em.createQuery("select b from Board b", Board.class)
                .getResultList();
        return result;
    }
}
