package com.nhnacademy.springsecurityfinal.service;

import com.nhnacademy.springsecurityfinal.model.MemberEntity;
import com.nhnacademy.springsecurityfinal.model.dto.MemberCreateRequest;
import com.nhnacademy.springsecurityfinal.model.dto.MemberResponse;

import java.util.List;

public interface MemberService {
    void createMember(MemberCreateRequest memberCreateRequest);

    MemberResponse getMember(String id);
    List<MemberResponse> getMembers();
    MemberEntity getMemberEntity(String id);
}
