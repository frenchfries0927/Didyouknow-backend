package com.example.didyouknow.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseHelper {

    // 성공 응답 (200 OK)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return respond(ResponseCode.SUCCESS, data);
    }

    // 생성 응답 (201 Created)
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.CREATED.getCode(), ResponseCode.CREATED.getMessage(), data));
    }

    // 에러 응답 - 기본 메시지
    public static <T> ResponseEntity<ApiResponse<T>> error(ResponseCode code) {
        return ResponseEntity.status(code.getCode())
                .body(ApiResponse.of(code.getCode(), code.getMessage(), null));
    }

    // 에러 응답 - 커스텀 메시지
    public static <T> ResponseEntity<ApiResponse<T>> error(ResponseCode code, String customMessage) {
        return ResponseEntity.status(code.getCode())
                .body(ApiResponse.of(code.getCode(), customMessage, null));
    }

    // 에러 응답 - HttpStatus 직접 지정
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.of(status.value(), message, null));
    }

    // 내부 공통 응답 빌더
    private static <T> ResponseEntity<ApiResponse<T>> respond(ResponseCode code, T data) {
        return ResponseEntity.status(code.getCode())
                .body(ApiResponse.of(code.getCode(), code.getMessage(), data));
    }
}
