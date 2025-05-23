package com.example.didyouknow.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnowledgePostRequest {
    private String title;
    private String content;
    private String publishDate; // "2025-05-07" 형식
}
