package com.example.didyouknow.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider;
    private String providerId;
    private String status;
    private String role;
    private Integer pushTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 관계 매핑 생략 (예: @OneToMany -> follows, posts, comments 등)
}
