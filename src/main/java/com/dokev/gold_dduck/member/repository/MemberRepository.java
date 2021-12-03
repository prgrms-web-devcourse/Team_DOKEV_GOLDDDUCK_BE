package com.dokev.gold_dduck.member.repository;

import com.dokev.gold_dduck.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT distinct e From Event e JOIN FETCH e.member")
    Member findMemberByEventId(@Param("id") Long eventId);
}
