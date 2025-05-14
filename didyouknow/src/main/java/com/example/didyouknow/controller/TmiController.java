package com.example.didyouknow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TmiController {

    @GetMapping("/api/tmi")
    public String getTodayTmi() {
        return "오늘의 TMI: 사람도 왼발잡이가 있듯이, 개도 있다!";
    }


}
