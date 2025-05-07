package com.example.didyouknow.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResponse {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
}
