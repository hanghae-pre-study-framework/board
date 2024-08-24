package com.hanhae99.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter @Setter
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long boardid;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "board_pwd")
    private String boardPwd;

    private Timestamp regDt;

    private String regId;

    private Timestamp chgDt;

}
