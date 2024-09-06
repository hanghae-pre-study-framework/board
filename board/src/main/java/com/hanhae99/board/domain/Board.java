package com.hanhae99.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter @Setter
@Builder
@Table(name = "boards")
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    private String userBoardId;

    @Column(name = "board_pwd", nullable = false)
    private String boardPwd;

    private Timestamp regDt;

    private String regId;

    private Timestamp chgDt;

}
