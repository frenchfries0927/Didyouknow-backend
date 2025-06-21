package com.example.didyouknow.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileUpdateRequest {
    String nickname;
    private MultipartFile file;
}
