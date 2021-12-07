package com.dokev.gold_dduck.event.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.dokev.gold_dduck.event.converter.EventFindConverter;
import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberDto;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
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
    private EventRepository eventRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EventSaveConverter eventSaveConverter;

    @Mock
    private EventFindConverter eventFindConverter;

    @Test
    @DisplayName("이벤트 코드 생성 테스트 - 성공")
    void eventCreateTest() {
        // GIVEN
        Member testMember = TestMemberFactory.createTestMember();

        Event newEvent = TestEventFactory.createEvent(testMember);

        EventSaveDto testEventSaveRequest = mock(EventSaveDto.class);

        given(memberRepository.findById(any())).willReturn(Optional.of(testMember));
        given(eventSaveConverter.convertToEvent(testEventSaveRequest, testMember)).willReturn(newEvent);
        given(eventRepository.save(newEvent)).willReturn(newEvent);

        // WHEN
        UUID eventCode = eventService.saveEvent(testEventSaveRequest);

        // THEN
        assertNotNull(eventCode);
        assertThat(eventCode, is(newEvent.getCode()));
    }

    @Test
    @DisplayName("이벤트 코드 통한 이벤트 조회 테스트 - 성공")
    void findDetailEventByCodeTest() {
        //given
        UUID eventCode = UUID.randomUUID();

        Member member = TestMemberFactory.createTestMember();

        Event event = TestEventFactory.builder(member)
                .code(eventCode)
                .build();

        EventDto eventDto = makeEventDto(eventCode);

        given(eventRepository.findGiftsByEventCode(eventCode)).willReturn(Optional.of(event));
        given(eventFindConverter.convertToEventDto(event)).willReturn(eventDto);

        //when
        EventDto foundEventDto = eventService.findDetailEventByCode(eventCode);

        //then
        Assertions.assertThat(foundEventDto.getCode()).isEqualTo(event.getCode());
        Assertions.assertThat(foundEventDto.getGifts().size()).isNotEqualTo(0);
    }

    private EventDto makeEventDto(UUID eventCode) {
        MemberDto memberDto = new MemberDto(1L, "dokev", "dokev@gmail.com", "id123",
                "http://dokev/image.jpg");

        GiftItemDto giftItemDto1 = new GiftItemDto(1L, GiftType.TEXT, "content1", false);
        GiftItemDto giftItemDto2 = new GiftItemDto(2L, GiftType.TEXT, "content2", false);
        GiftItemDto giftItemDto3 = new GiftItemDto(3L, GiftType.TEXT, "content3", false);
        GiftItemDto giftItemDto4 = new GiftItemDto(4L, GiftType.TEXT, "content4", false);

        ArrayList<GiftItemDto> giftItemDtos1 = new ArrayList<>();
        ArrayList<GiftItemDto> giftItemDtos2 = new ArrayList<>();
        giftItemDtos1.add(giftItemDto1);
        giftItemDtos1.add(giftItemDto2);
        giftItemDtos2.add(giftItemDto3);
        giftItemDtos2.add(giftItemDto4);

        GiftDto giftDto1 = new GiftDto(1L, "coffee", 10, giftItemDtos1);
        GiftDto giftDto2 = new GiftDto(2L, "book", 10, giftItemDtos2);
        ArrayList<GiftDto> giftDtos = new ArrayList<>();
        giftDtos.add(giftDto1);
        giftDtos.add(giftDto2);

        return new EventDto(1L, GiftChoiceType.FIFO, LocalDateTime.now(), LocalDateTime.now(),
                eventCode, "진행상태", "mainTemplate1", 10, memberDto,
                giftDtos);
    }
}
