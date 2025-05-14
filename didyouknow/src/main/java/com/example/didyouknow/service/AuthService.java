package com.example.didyouknow.service;

import com.example.didyouknow.auth.JwtProvider;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.user.GoogleUserInfo;
import com.example.didyouknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public Map<String, Object> loginOrSignupWithGoogle(String idToken) {
        GoogleUserInfo userInfo = googleAuthService.verifyAndGetUserInfo(idToken);

        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(userInfo.getEmail());
            newUser.setNickname(userInfo.getName());
            newUser.setProfileImageUrl("https://example.com/profile_minjihye.jpg");
            newUser.setProvider("google");
            newUser.setProviderId(userInfo.getSub());
            newUser.setRole("USER");
            newUser.setStatus("ACTIVE");
            newUser.setPushTime(9);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(newUser);
        });

        String jwt = jwtProvider.generateToken(user.getEmail(), user.getNickname());

        return Map.of(
                "token", jwt,
                "user", userInfo
        );
    }

    public boolean verifyToken(String token) {
        return jwtProvider.validateToken(token);
    }
}