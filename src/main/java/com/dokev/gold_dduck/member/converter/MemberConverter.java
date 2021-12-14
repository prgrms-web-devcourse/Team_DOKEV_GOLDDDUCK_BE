package com.dokev.gold_dduck.member.converter;

import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberDto;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public MemberDto convertToMemberDto(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getEmail(),
            member.getSocialId(), member.getProfileImage().orElse(null));
    }
}
