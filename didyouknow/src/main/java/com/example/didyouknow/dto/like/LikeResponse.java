
package com.example.didyouknow.dto.like;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private String targetType;
    private Long targetId;
    private String createdAt;
}
