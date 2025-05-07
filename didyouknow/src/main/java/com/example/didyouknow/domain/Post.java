package com.example.didyouknow.domain;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String postType;

    @Column(columnDefinition = "JSONB")
    private String slidesJson;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
}
