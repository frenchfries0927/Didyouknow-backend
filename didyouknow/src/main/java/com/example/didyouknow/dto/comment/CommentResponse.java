package com.example.didyouknow.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String writerNickname;
    private String createdAt;
}
