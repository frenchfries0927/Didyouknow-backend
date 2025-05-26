package com.example.didyouknow.controller;

import com.example.didyouknow.auth.JwtProvider;
import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<Map<String, Object>>> googleLogin(@RequestBody Map<String, String> request) {
        String idToken = request.get("token");
        Map<String, Object> loginResult = authService.loginOrSignupWithGoogle(idToken);

        ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                .code(200)
                .message("Google 로그인 성공")
                .data(loginResult)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean isValid = authService.verifyToken(token);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(isValid ? 200 : 401)
                .message(isValid ? "유효한 토큰입니다" : "토큰이 유효하지 않습니다")
                .data(null)
                .build();

        return isValid ? ResponseEntity.ok(response) : ResponseEntity.status(401).body(response);
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<ApiResponse<Map<String, String>>> reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Map<String, String> tokenResponse = authService.reissueAccessToken(refreshToken);

        if (tokenResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.of(401, "Invalid refresh token", null));
        }

        return ResponseEntity.ok(ApiResponse.of(200, "재발급 성공", tokenResponse));
    }

}