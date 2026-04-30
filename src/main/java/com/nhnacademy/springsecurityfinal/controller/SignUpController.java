package com.nhnacademy.springsecurityfinal.controller;

import com.nhnacademy.springsecurityfinal.model.dto.MemberCreateRequest;
import com.nhnacademy.springsecurityfinal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody MemberCreateRequest request) {
        memberService.createMember(request);
        return ResponseEntity.ok().build(); // TODO created?
    }
}
