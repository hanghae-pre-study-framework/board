package com.hanhae99.board.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "id")
    private String id;

    private String pwd;

    private String token;

    @Column(name = "user_name")
    private String userName;

    private Timestamp regDt;

    private String regId;

    private Timestamp chgDt;

    private String chgId;

    //@OneToMany(mappedBy = "user")
    //private List<Board> boards = new ArrayList<>();


}
