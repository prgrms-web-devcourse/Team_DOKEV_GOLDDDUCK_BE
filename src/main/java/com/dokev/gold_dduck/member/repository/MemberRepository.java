package com.dokev.gold_dduck.member.repository;

import com.dokev.gold_dduck.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
