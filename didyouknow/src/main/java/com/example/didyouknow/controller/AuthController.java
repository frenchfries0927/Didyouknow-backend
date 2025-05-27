package com.example.didyouknow.controller;

import com.example.didyouknow.common.ApiResponse;
import com.example.didyouknow.common.ApiResponseHelper;
import com.example.didyouknow.common.ResponseCode;
import com.example.didyouknow.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "OAuth API", description = "소셜 로그인 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "구글 회원가입 & 로그인",
            description = "구글 계정으로 로그인 시 받아온 토큰을 통해 회원가입 및 로그인",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "구글 로그인 시 받아온 Token",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/google")
    public ResponseEntity<ApiResponse<Map<String, Object>>> googleLogin(@RequestBody Map<String, String> request) {
        String idToken = request.get("token");
        Map<String, Object> loginResult = authService.loginOrSignupWithGoogle(idToken);
        return ApiResponseHelper.success(loginResult);
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Map<String, String>>> verifyToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean isValid = authService.verifyToken(token);

        if (isValid) {
            return ApiResponseHelper.success(null);
        } else {
            return ApiResponseHelper.error(ResponseCode.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<ApiResponse<Map<String, String>>> reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Map<String, String> tokenResponse = authService.reissueAccessToken(refreshToken);

        if (tokenResponse == null) {
            return ApiResponseHelper.error(ResponseCode.UNAUTHORIZED);
        }

        return ApiResponseHelper.success(tokenResponse);
    }
}
