package com.dokev.gold_dduck.member.service;

import com.dokev.gold_dduck.member.domain.Group;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.GroupRepository;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final GroupRepository groupRepository;

    public MemberService(MemberRepository memberRepository,
        GroupRepository groupRepository) {
        this.memberRepository = memberRepository;
        this.groupRepository = groupRepository;
    }

    public Optional<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    public Optional<Member> findByProviderAndSocialId(String provider, String socialId) {
        return memberRepository.findByProviderAndSocialId(provider, socialId);
    }

    @Transactional
    public Member join(OAuth2User oauth2User, String provider) {
        String socialId = oauth2User.getName();
        return findByProviderAndSocialId(provider, socialId)
            .map(member -> {
                log.warn("Already exists: {} for provider: {} socialId: {}", member, provider, socialId);
                return member;
            })
            .orElseGet(() -> {
                Map<String, Object> attributes = oauth2User.getAttributes();
                @SuppressWarnings("unchecked")
                Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
                Objects.requireNonNull(properties, "OAuth2User properties is empty");

                String nickname = (String) properties.get("nickname");
                String profileImage = (String) properties.get("profile_image");
                Group group = groupRepository.findByName("USER_GROUP")
                    .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));
                return memberRepository.save(new Member(nickname, provider, socialId, profileImage, group));
            });
    }
}
