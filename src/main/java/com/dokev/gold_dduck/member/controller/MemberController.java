package com.dokev.gold_dduck.member.controller;

import com.dokev.gold_dduck.common.ApiResponse;
import com.dokev.gold_dduck.jwt.JwtAuthentication;
import com.dokev.gold_dduck.member.dto.MemberDto;
import com.dokev.gold_dduck.member.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/v1/members/me")
    public ApiResponse<MemberDto> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        MemberDto memberDto = memberService.findById(authentication.userId);
        return ApiResponse.success(memberDto);
    }
}
