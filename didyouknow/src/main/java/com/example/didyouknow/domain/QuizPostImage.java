package com.example.didyouknow.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_post_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizPostImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @Column(name = "sequence")
    private Integer sequence;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_post_id")
    private QuizPost quizPost;
} 