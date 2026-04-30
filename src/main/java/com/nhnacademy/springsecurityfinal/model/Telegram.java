package com.nhnacademy.springsecurityfinal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Telegram {
    @JsonProperty("chat_id")
    private String chatId;
    private String text;
}
