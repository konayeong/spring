package com.nhnacademy.springsecurityfinal.controller;

import com.nhnacademy.springsecurityfinal.model.dto.MemberResponse;
import com.nhnacademy.springsecurityfinal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // id 중복 불가
    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable("id") String id) {
        return memberService.getMember(id);
    }

    @GetMapping
    public List<MemberResponse> getMembers(Pageable pageable) {
        System.out.println("page : " + pageable.getPageNumber());
        System.out.println("size : " + pageable.getPageSize());
        return memberService.getMembers();
    }

}
