package com.example.didyouknow.dto.bookmark;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookmarkResponse {
    private String targetType;
    private Long targetId;
    private String createdAt;
}
