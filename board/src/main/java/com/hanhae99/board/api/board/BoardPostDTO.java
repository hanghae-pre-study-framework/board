package com.hanhae99.board.api.board;

import com.hanhae99.board.domain.Board;
import com.hanhae99.board.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPostDTO {

    private String title;
    private String content;
    private String userId;
    private String boardPwd;
    @Builder
    public BoardPostDTO(String title, String content, String userId, String boardPwd){
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.boardPwd = boardPwd;

    }

    public Board toEntity(){

        return Board.builder()
                .title(title)
                .content(content)
                .userBoardId(userId)
                .boardPwd(boardPwd)
                .regId(userId)
                .build();

    }



}
