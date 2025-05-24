package com.example.didyouknow.controller;

import com.example.didyouknow.dto.user.ProfileRequest;
import com.example.didyouknow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/me/complete-profile")
    public ResponseEntity<?> completeProfile(@RequestBody ProfileRequest request,
                                             @AuthenticationPrincipal Long userId) {
        System.out.println(request.toString());
        userService.completeProfile(userId, request);
        return ResponseEntity.ok().build();
    }
}
