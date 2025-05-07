package com.example.didyouknow.dto.comment;

import lombok.Getter;

@Getter
public class CommentRequest {
    private String targetType;  // "post" or "quiz"
    private Long targetId;
    private Long parentCommentId; // 대댓글일 경우만
    private String content;
}
