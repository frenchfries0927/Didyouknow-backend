package com.example.didyouknow.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KnowledgePostResponse {
    private Long id;
    private String type;
    private String title;
    private String content;
    private String authorNickname;
    private String publishDate;
    private List<String> imageUrls;
    private Long likes;
    private Long comments;
    private Boolean isLiked;
}
