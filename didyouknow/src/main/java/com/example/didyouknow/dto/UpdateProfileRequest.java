package com.example.didyouknow.dto;



import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String nickname;
    private String imageUrl;
}
