package com.example.didyouknow.dto.user;



import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;
    private String nickname;
    private String password;
}
