package com.example.didyouknow.dto.quiz;

import java.util.List;

public class QuizPostResponse {
    private Long id;
    private String question;
    private String[] options;
    private String authorNickname;
    private String publishDate;
    private List<String> imageUrls;
    private Long likes;
    private Long comments;
    private Boolean isLiked;

    public QuizPostResponse(Long id, String question, String[] options, String authorNickname, 
                           String publishDate, List<String> imageUrls, Long likes, Long comments, Boolean isLiked) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.authorNickname = authorNickname;
        this.publishDate = publishDate;
        this.imageUrls = imageUrls;
        this.likes = likes;
        this.comments = comments;
        this.isLiked = isLiked;
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public Long getLikes() {
        return likes;
    }

    public Long getComments() {
        return comments;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }
}
