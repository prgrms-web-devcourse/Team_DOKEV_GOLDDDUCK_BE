package com.dokev.gold_dduck.event.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.dokev.gold_dduck.config.JpaAuditingConfiguration;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.member.domain.Member;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@Import(JpaAuditingConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class EventLogRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventLogRepository sut;

    @Test
    @DisplayName("Event Id와 Member Id를 조건으로 일치하는 EventLog 검색 - 성공 테스트")
    void existsByEventIdAndMemberIdSuccessTest() {
        //given
        Member member = TestMemberFactory.givenMembers(entityManager).getUserMember();
        Event event = TestEventFactory.builder(member).build();
        entityManager.persist(event);
        entityManager.persist(new EventLog(event, member, null, null));
        //when
        boolean existed1 = sut.existsByEventIdAndMemberId(event.getId(), member.getId());
        boolean existed2 = sut.existsByEventIdAndMemberId(-1L, member.getId());
        boolean existed3 = sut.existsByEventIdAndMemberId(event.getId(), -1L);
        boolean existed4 = sut.existsByEventIdAndMemberId(-1L, -1L);
        //then
        assertThat(existed1).isTrue();
        assertThat(existed2).isFalse();
        assertThat(existed3).isFalse();
        assertThat(existed4).isFalse();
    }
}
