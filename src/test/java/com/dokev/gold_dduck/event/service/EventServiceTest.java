package com.dokev.gold_dduck.event.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.dto.GiftSaveDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestEventSaveRequestFactory;
import com.dokev.gold_dduck.factory.TestGiftFactory;
import com.dokev.gold_dduck.factory.TestGiftItemFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.repository.GiftItemRepository;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private GiftRepository giftRepository;

    @Mock
    private GiftItemRepository giftItemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EventSaveConverter eventConverter;

    @Mock
    private EventRepository eventRepository;

    @Test
    @DisplayName("이벤트 코드 생성 테스트 - 성공")
    void eventCreateTest() {
        Member testMember = TestMemberFactory.createTestMember();
        EventSaveDto testEventSaveRequest = TestEventSaveRequestFactory.createTestEventSaveRequest();
        Event newEvent = TestEventFactory.dtoUseBuilder(testEventSaveRequest, testMember).build();

        List<GiftSaveDto> giftSaveDtos = testEventSaveRequest.getGifts();

        given(memberRepository.findById(any())).willReturn(Optional.of(testMember));
        given(eventConverter.convertToEvent(testEventSaveRequest, testMember)).willReturn(newEvent);

        List<Gift> gifts = new ArrayList<>();
        List<GiftItem> giftItems = new ArrayList<>();

        giftSaveDtos.forEach(giftSaveDto -> {
            Gift testGift = TestGiftFactory.createTestGift(
                giftSaveDto.getCategory(),
                giftSaveDto.getGiftItems().size()
            );
            given(eventConverter.convertToGift(giftSaveDto)).willReturn(testGift);
            gifts.add(testGift);

            giftSaveDto.getGiftItems().forEach(giftItemSaveDto -> {
                GiftItem testGiftItem = TestGiftItemFactory.createTestGiftItem(giftItemSaveDto.getContent());
                given(eventConverter.convertToGiftItem(giftItemSaveDto)).willReturn(testGiftItem);
                giftItems.add(testGiftItem);
            });
        });

        given(giftItemRepository.saveAll(giftItems)).willReturn(giftItems);
        given(giftRepository.saveAll(gifts)).willReturn(gifts);
        given(eventRepository.save(newEvent)).willReturn(newEvent);

        UUID eventCode = eventService.saveEvent(testEventSaveRequest);

        assertNotNull(eventCode);
        assertThat(eventCode, is(newEvent.getCode()));
    }
}
