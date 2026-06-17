package com.nhnacademy.flyschedule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LlmType {
    OLLAMA,
    GEMINI;

    @JsonCreator
    public static LlmType from(String value) {
        return LlmType.valueOf(value.toUpperCase());
    }
}
