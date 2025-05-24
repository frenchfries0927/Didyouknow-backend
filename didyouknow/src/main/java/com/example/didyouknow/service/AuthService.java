package com.example.didyouknow.service;

import com.example.didyouknow.auth.JwtProvider;
import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.user.GoogleUserInfo;
import com.example.didyouknow.dto.user.UserResponse;
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
        // 1. 구글 토큰 검증
        GoogleUserInfo userInfo = googleAuthService.verifyAndGetUserInfo(idToken);

        // 2. DB 저장 또는 기존 유저 조회
        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(userInfo.getEmail());
            newUser.setNickname(null);
            newUser.setProfileImageUrl(userInfo.getPicture()); // 또는 기본 이미지
            newUser.setProvider("google");
            newUser.setProviderId(userInfo.getSub());
            newUser.setRole("USER");
            newUser.setStatus("REGISTERED"); // 회원가입만 완료됨
            newUser.setPushTime(null);       // 알림 설정도 아직 안 한 상태
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(newUser);
        });

        // 3. JWT 발급
        String jwt = jwtProvider.generateToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus());

        // 4. 유저 정보 DTO 가공
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .build();

        // 5. 응답 반환
        return Map.of(
                "token", jwt,
                "user", userResponse,
                "requiresProfile", user.getStatus().equals("REGISTERED")
        );
    }

    public boolean verifyToken(String token) {
        return jwtProvider.validateToken(token);
    }
}
