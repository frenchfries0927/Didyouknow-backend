package com.example.didyouknow.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ProfileRequest {
    private String nickname;
    @JsonProperty("alarmHour")
    private Integer pushTime;
    private boolean alarmEnabled;
}
