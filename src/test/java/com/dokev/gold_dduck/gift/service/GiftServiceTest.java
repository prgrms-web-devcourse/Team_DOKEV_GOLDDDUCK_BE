package com.dokev.gold_dduck.gift.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.dokev.gold_dduck.common.exception.GiftBlankDrawnException;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.repository.EventLogRepository;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.gift.dto.GiftItemDetailDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchDto;
import com.dokev.gold_dduck.gift.repository.GiftItemRepository;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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

    @Test
    @DisplayName("랜덤으로 선물 받기 성공 테스트")
    void chooseGiftItemByRandomSuccessTest() {
        //given
        //adminUser는 이벤트에 여러번 참여할 수 있다.
        Member savedMember = TestMemberFactory.getAdminMember(entityManager);
        Event savedEvent = eventRepository.save(TestEventFactory.builder(savedMember).leftBlankCount(2).build());
        Gift gift = new Gift("커피", 3);
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
        List<String> contents = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            try {
                GiftItemDetailDto giftItemDto = sut.chooseGiftItemByRandom(savedEvent.getId(), savedMember.getId());
                contents.add(giftItemDto.getContent());
            } catch (GiftBlankDrawnException e) {
                contents.add("꽝");
            }
        }
        //then
        assertThat(contents, hasSize(5));
        assertThat(contents, containsInAnyOrder("coffee1", "꽝", "coffee2", "꽝", "coffee3"));
        boolean alreadyParticipated = eventLogRepository.existsByEventIdAndMemberId(savedEvent.getId(),
            savedMember.getId());
        assertThat(alreadyParticipated, is(true));
    }

    @Test
    @DisplayName("giftItem의 id를 통해 giftItem 검색 테스트 - 성공")
    void searchGiftItemById() {
        Member admin = TestMemberFactory.getAdminMember(entityManager);
        Member user = TestMemberFactory.getUserMember(entityManager);
        Event event = eventRepository.save(TestEventFactory.createEvent(admin));

        Gift gift = new Gift("커피", 3);
        gift.changeEvent(event);
        Gift savedGift = giftRepository.save(gift);

        GiftItem giftItem = new GiftItem(GiftType.TEXT, "coffee1", false);
        giftItem.changeGift(savedGift);
        giftItem.allocateMember(user);
        giftItemRepository.save(giftItem);

        EventLog eventLog = new EventLog(event, user, gift, giftItem);
        eventLogRepository.save(eventLog);

        GiftItemSearchDto giftItemSearchDto = sut.searchGiftItem(giftItem.getId());

        Assertions.assertThat(giftItemSearchDto.getSender()).isEqualTo(event.getMember().getName());
        Assertions.assertThat(giftItemSearchDto.getMainTemplate()).isEqualTo(event.getMainTemplate());
        Assertions.assertThat(giftItemSearchDto.getCategory()).isEqualTo(gift.getCategory());
    }
}
