package com.nhnacademy.springsecurityfinal.service;

import com.nhnacademy.springsecurityfinal.model.Telegram;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramSender {
    private static final String TOKEN = "8643772327:AAHK80ne_9pqHnrfm5Y_XIUXnoGSOHAWwcU";
    private RestTemplate restTemplate;

    public TelegramSender() {
        restTemplate = new RestTemplate();
    }

    public void send(String text) {
        Telegram request = new Telegram("8575046026", text);
        String url = "https://api.telegram.org/bot" + TOKEN + "/sendMessage";

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println(response.getBody());
    }
}