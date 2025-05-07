package com.example.didyouknow.service;



import com.example.didyouknow.domain.User;
import com.example.didyouknow.dto.*;
import com.example.didyouknow.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signup(SignupRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); // 실제 구현 시 암호화 필요
        user.setNickname(req.getNickname());
        userRepository.save(user);
    }

    public String login(LoginRequest req) {
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("유저 없음");
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }
        return "mock-token"; // 실제로는 JWT 반환
    }

    public UserProfile getMyProfile() {
        // 실제로는 인증된 사용자 기준으로 반환해야 함
        return new UserProfile("홍길동", "https://example.com/image.png");
    }

    public void updateProfile(UpdateProfileRequest req) {
        // 인증 처리 후 사용자 정보 수정
    }
}
