package com.example.didyouknow.dto.badge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadgeResponse {
    private Long id;
    private String code;
    private String name;
}
