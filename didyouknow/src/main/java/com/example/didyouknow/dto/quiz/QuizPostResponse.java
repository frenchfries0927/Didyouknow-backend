package com.example.didyouknow.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizPostResponse {
    private Long id;
    private String question;
    private String[] options;
    private String authorNickname;
    private String publishDate;
}
