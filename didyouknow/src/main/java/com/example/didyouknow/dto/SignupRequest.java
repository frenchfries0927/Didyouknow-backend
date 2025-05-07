package com.example.didyouknow.dto;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;
    private String nickname;
    private String password;
}
