package com.example.didyouknow.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserInfo {
    private String sub;       // 고유 ID
    private String email;
    private String name;
    private String picture;
}
