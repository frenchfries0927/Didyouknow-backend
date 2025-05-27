package com.example.didyouknow.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(200, "성공"),
    CREATED(201, "생성 완료"),
    BAD_REQUEST(400, "잘못된 요청입니다"),
    UNAUTHORIZED(401, "인증이 필요합니다"),
    FORBIDDEN(403, "접근 권한이 없습니다"),
    NOT_FOUND(404, "리소스를 찾을 수 없습니다"),
    SERVER_ERROR(500, "서버 오류가 발생했습니다");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
