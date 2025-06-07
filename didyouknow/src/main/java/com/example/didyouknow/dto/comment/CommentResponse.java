package com.example.didyouknow.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String author;
    private Long authorId;
    private String profileImageUrl;
    private String createdAt;
    private Long parentCommentId;
    private int likes;
    private List<CommentResponse> replies;
}
