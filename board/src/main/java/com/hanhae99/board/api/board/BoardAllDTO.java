package com.hanhae99.board.api.board;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class BoardAllDTO {
    private String title;
    private String regName;
    private String content;
    private Timestamp regDate;

}

