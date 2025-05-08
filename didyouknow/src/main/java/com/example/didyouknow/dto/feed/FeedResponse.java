package com.example.didyouknow.dto.feed;

import java.time.LocalDateTime;

public class FeedResponse {
    private Long id;
    private String type; // "knowledge" or "quiz"
    private String title;
    private String content; // knowledgePost.content 또는 quizPost의 정답 정보 등
    private String imageUrl; // quizPost 전용
    private String authorNickname;
    private String authorProfileImageUrl;
    private LocalDateTime createdAt;

    public FeedResponse(Long id, String type, String title, String content, String imageUrl,
                        String authorNickname, String authorProfileImageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.authorNickname = authorNickname;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.createdAt = createdAt;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public String getAuthorProfileImageUrl() {
        return authorProfileImageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
