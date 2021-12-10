package com.dokev.gold_dduck.event.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.dokev.gold_dduck.config.JpaAuditingConfiguration;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.security.WithMockJwtAuthentication;
import javax.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@WithMockJwtAuthentication
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
        Member member = TestMemberFactory.getUserMember(entityManager);
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

    @Test
    @DisplayName("Event id가 주어진 eventId와 일치하고 선물을 받은 로그만 검색 - 성공 테스트")
    void findWinnerLogsByEventIdTest() {
        //given
        Member member1 = TestMemberFactory.createTestMember(entityManager);
        entityManager.persist(member1);

        Member member2 = TestMemberFactory.createTestMember(entityManager);
        entityManager.persist(member2);

        Member member3 = TestMemberFactory.createTestMember(entityManager);
        entityManager.persist(member3);

        Event event = TestEventFactory.builder(member1).build();
        entityManager.persist(event);

        Gift gift1 = new Gift("coffee", 2);
        gift1.changeEvent(event);
        entityManager.persist(gift1);

        Gift gift2 = new Gift("chicken", 2);
        gift2.changeEvent(event);
        entityManager.persist(gift2);

        GiftItem giftItem1 = new GiftItem(GiftType.TEXT, "content1", true);
        GiftItem giftItem2 = new GiftItem(GiftType.TEXT, "content2", true);
        GiftItem giftItem3 = new GiftItem(GiftType.TEXT, "content3", true);
        GiftItem giftItem4 = new GiftItem(GiftType.TEXT, "content4", true);

        giftItem1.changeGift(gift1);
        giftItem2.changeGift(gift1);
        giftItem3.changeGift(gift2);
        giftItem4.changeGift(gift2);

        entityManager.persist(giftItem1);
        entityManager.persist(giftItem2);
        entityManager.persist(giftItem3);
        entityManager.persist(giftItem4);

        entityManager.clear();

        entityManager.persist(new EventLog(event, member2, gift1, giftItem1));
        entityManager.persist(new EventLog(event, member2, gift2, giftItem3));

        List<EventLog> eventLogs = sut.findWinnerLogsByEventId(event.getId());

        assertThat(eventLogs.size()).isEqualTo(2);
        assertThat(eventLogs.get(0).getGift()).isEqualTo(gift1);
        assertThat(eventLogs.get(1).getGift()).isEqualTo(gift2);
        assertThat(eventLogs.get(0).getGiftItem().getContent()).isEqualTo(giftItem1.getContent());
        assertThat(eventLogs.get(1).getGiftItem().getContent()).isEqualTo(giftItem3.getContent());
    }
}
