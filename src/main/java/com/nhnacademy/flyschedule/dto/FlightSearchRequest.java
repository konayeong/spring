package com.nhnacademy.flyschedule.dto;

/**
 * ollama / gemini
 * coordinator / orchestration
 */
public record FlightSearchRequest (
        String message,
        LlmType llmType
){}
