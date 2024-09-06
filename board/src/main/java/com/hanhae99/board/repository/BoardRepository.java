package com.hanhae99.board.repository;

import com.hanhae99.board.api.board.BoardAllDTO;
import com.hanhae99.board.domain.Board;
import com.hanhae99.board.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     * 내림차순 전체 게시물 조회
     * @return BoardAllDTO
     */
    @Query("SELECT new com.hanhae99.board.api.board.BoardAllDTO(b.title, u.userName, b.content, b.regDt) " +
            "FROM Board b JOIN b.user u  " +
            "ORDER BY b.regDt DESC")
    List<BoardAllDTO> findBoardByUserId();
}
