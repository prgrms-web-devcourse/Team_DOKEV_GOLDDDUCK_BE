package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.config.JpaAuditingConfiguration;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.member.domain.Member;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(JpaAuditingConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("이벤트 코드를 통한 단일 이벤트 조회 테스트")
    void findGiftsByEventCode() {
        UUID eventCode = UUID.randomUUID();

        Member member = TestMemberFactory.getUserMember(entityManager);

        Event event = TestEventFactory.builder(member)
            .code(eventCode)
            .build();
        entityManager.persist(event);

        Event deletedEvent = TestEventFactory.createEvent(member);
        deletedEvent.deleteEvent();
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
        Optional<Event> findByCodeEvent = eventRepository.findEventByCodeWithGift(eventCode);
        Optional<Event> foundDeletedEvent = eventRepository.findEventByCodeWithGift(deletedEvent.getCode());

        Assertions.assertThat(findByCodeEvent.get().getId()).isEqualTo(event.getId());
        Assertions.assertThat(findByCodeEvent.get().getGifts().size()).isEqualTo(2);
        Assertions.assertThat(findByCodeEvent.get().getGifts().get(0).getGiftItems().size()).isEqualTo(2);

        Assertions.assertThat(foundDeletedEvent.isPresent()).isFalse();
    }
}
