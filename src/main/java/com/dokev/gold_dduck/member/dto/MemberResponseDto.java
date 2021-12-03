package com.dokev.gold_dduck.member.dto;

import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long id;

    private String name;

    private String email;

    private String socialId;

    private String profileImage;

    public MemberResponseDto(Long id, String name, String email, String socialId, String profileImage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.profileImage = profileImage;
    }
}
