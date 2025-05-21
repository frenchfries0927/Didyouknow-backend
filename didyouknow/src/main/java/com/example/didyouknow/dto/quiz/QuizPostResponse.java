package com.example.didyouknow.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuizPostResponse {
    private Long id;
    private String question;
    private String[] options;
    private String authorNickname;
    private String publishDate;
    private List<String> imageUrls;
}
