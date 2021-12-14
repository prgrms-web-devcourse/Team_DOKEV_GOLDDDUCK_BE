package com.dokev.gold_dduck.gift.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.dokev.gold_dduck.config.JpaAuditingConfiguration;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.repository.EventLogRepository;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.converter.GiftConverter;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.gift.repository.GiftItemQueryRepository;
import com.dokev.gold_dduck.gift.repository.GiftItemRepository;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import({JpaAuditingConfiguration.class, GiftService.class, GiftItemQueryRepository.class, GiftConverter.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class GiftServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private GiftItemRepository giftItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EventLogRepository eventLogRepository;

    @Autowired
    private GiftService sut;

    @Test
    @DisplayName("선착순으로 선물 받기 성공 테스트")
    void chooseGiftItemByFIFOSuccessTest() {
        //given
        Member savedMember = TestMemberFactory.getUserMember(entityManager);
        Event savedEvent = eventRepository.save(TestEventFactory.builder(savedMember).build());
        Gift gift = new Gift("커피", 20);
        gift.changeEvent(savedEvent);
        Gift savedGift = giftRepository.save(gift);
        GiftItem giftItem1 = new GiftItem(GiftType.TEXT, "coffee1", false);
        GiftItem giftItem2 = new GiftItem(GiftType.TEXT, "coffee2", false);
        GiftItem giftItem3 = new GiftItem(GiftType.IMAGE, "coffee3", false);
        giftItem1.changeGift(savedGift);
        giftItem2.changeGift(savedGift);
        giftItem3.changeGift(savedGift);
        giftItemRepository.save(giftItem1);
        giftItemRepository.save(giftItem2);
        giftItemRepository.save(giftItem3);
        //when
        GiftItemDto giftItemDto = sut.chooseGiftItemByFIFO(savedEvent.getId(), savedMember.getId(), savedGift.getId());
        //then
        assertThat(giftItemDto.getContent(), is("coffee1"));
        boolean alreadyParticipated = eventLogRepository.existsByEventIdAndMemberId(savedEvent.getId(),
            savedMember.getId());
        assertThat(alreadyParticipated, is(true));

    }
}
