package com.dokev.gold_dduck.member.converter;

import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberDto;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public MemberDto convertToMemberDto(Member member) {
        if (member.getProfileImage().isEmpty()) {
            return new MemberDto(member.getId(), member.getName(), member.getEmail(),
                    member.getSocialId(), "null");
        }

        return new MemberDto(member.getId(), member.getName(), member.getEmail(),
                member.getSocialId(), member.getProfileImage().get());
    }
}
