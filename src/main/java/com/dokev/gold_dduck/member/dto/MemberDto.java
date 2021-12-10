package com.dokev.gold_dduck.member.dto;

import com.dokev.gold_dduck.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberDto {

    private Long id;

    private String name;

    private String email;

    private String socialId;

    private String profileImage;

    public MemberDto(Long id, String name, String email, String socialId, String profileImage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.profileImage = profileImage;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.profileImage = member.getProfileImage().orElse(null);
    }
}
