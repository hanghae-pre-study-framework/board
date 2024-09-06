package com.hanhae99.board.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    int count; // 총 조회수
    private T data;
    private String message;
    private int status;
}
