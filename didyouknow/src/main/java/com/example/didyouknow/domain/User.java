package com.example.didyouknow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    private String status;
    private String role;

    @Column(name = "push_time")
    private Integer pushTime;

    @Column(nullable = true)
    private boolean alarmEnabled = true;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
