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
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestGiftFactory;
import com.dokev.gold_dduck.factory.TestGiftItemFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberDto;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.List;
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
        Assertions.assertThat(foundEventDto.getGifts().size()).isEqualTo(3);

    }

    private EventDto makeEventDto(UUID eventCode) {
        MemberDto memberDto = new MemberDto(1L, "dokev", "dokev@gmail.com", "id123",
                "http://dokev/image.jpg");

        List<GiftItemDto> giftItemDtos = TestGiftItemFactory.createTestGiftItemDtos();

        List<GiftDto> giftDtos = TestGiftFactory.createTestGiftDtos(giftItemDtos);

        return TestEventFactory.createEventDto(eventCode, memberDto, giftDtos);
    }
}
