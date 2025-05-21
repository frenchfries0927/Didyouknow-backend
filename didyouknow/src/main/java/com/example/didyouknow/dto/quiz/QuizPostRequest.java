package com.example.didyouknow.dto.quiz;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizPostRequest {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Integer correctOption;
    private String publishDate;
}
