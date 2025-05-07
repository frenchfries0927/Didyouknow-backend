package com.example.didyouknow.service;

import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.user.SignupRequest;
import com.example.didyouknow.dto.user.UserResponse;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse signup(SignupRequest request) {
        // 이메일 중복 + 같은 providerId가 있을 경우 중복 가입 방지
        boolean userExists = userRepository.findByEmail(request.getEmail())
                .filter(u -> u.getProviderId().equals(request.getProviderId()))
                .isPresent();

        if (userExists) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setProvider(request.getProvider());
        user.setProviderId(request.getProviderId());
        user.setRole("USER");
        user.setStatus("ACTIVE");
        user.setPushTime(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getNickname(),
                saved.getProfileImageUrl()
        );
    }
}
