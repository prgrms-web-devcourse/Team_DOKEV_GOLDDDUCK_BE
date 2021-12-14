package com.dokev.gold_dduck.member.repository;

import com.dokev.gold_dduck.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findSimpleById(Long id);

    @Query("select m from Member m"
        + " join fetch m.group g"
        + " where m.id = :id")
    Optional<Member> findByIdWithGroup(@Param("id") Long id);

    @Query("select m from Member m"
        + " join fetch m.group g"
        + " left join fetch g.groupPermissions gp"
        + " left join fetch gp.permission p"
        + " where m.provider = :provider and m.socialId = :socialId")
    Optional<Member> findByProviderAndSocialId(@Param("provider") String provider, @Param("socialId") String socialId);
}
