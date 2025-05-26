package com.example.didyouknow.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int code;      // ex) 400, 404, 500
    private String message; // ex) "Validation failed", "User not found"
    private T data;

    public static <T> ApiResponse<T> of(int code, String message, T data) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}
