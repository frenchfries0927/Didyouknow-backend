package com.example.didyouknow.dto.quiz;



import lombok.Getter;

@Getter
public class QuizPostRequest {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption;
    private String publishDate;
}
