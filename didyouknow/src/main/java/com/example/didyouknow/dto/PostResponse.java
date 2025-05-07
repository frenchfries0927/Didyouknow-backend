package com.example.didyouknow.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private String title;
    private String content;
    private String authorName;
    private String postType;
    private LocalDateTime createdAt;
}
