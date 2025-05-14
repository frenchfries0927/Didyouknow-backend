package com.example.didyouknow.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@AllArgsConstructor
public class FeedResponse {
    private Long id;
    private String type; // "knowledge" or "quiz"
    private String title;
    private String content;
    private String imageUrl;
    private String author;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private List<String> options; // 추가
}
