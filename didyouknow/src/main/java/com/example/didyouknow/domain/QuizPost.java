package com.example.didyouknow.domain;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_post")
public class QuizPost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Integer correctOption;

    private LocalDate publishDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
