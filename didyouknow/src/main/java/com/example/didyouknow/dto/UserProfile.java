package com.example.didyouknow.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfile {
    private String nickname;
    private String imageUrl;
}
