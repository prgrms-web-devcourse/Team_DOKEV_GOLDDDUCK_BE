package com.dokev.gold_dduck.member.converter;

import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public MemberResponseDto convertToMemberResponseDto(Member member) {
        if (member.getProfileImage().isEmpty()) {
            return new MemberResponseDto(member.getId(), member.getName(), member.getEmail(),
                    member.getSocialId(), "null");
        }
        return new MemberResponseDto(member.getId(), member.getName(), member.getEmail(),
                member.getSocialId(), member.getProfileImage().get());
    }

}
