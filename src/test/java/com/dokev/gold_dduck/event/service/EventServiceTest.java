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
import com.dokev.gold_dduck.gift.converter.GiftConverter;
import com.dokev.gold_dduck.member.converter.MemberConverter;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
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
        Member testMember = mock(Member.class);

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
        Member member = mock(Member.class);

        Event event = TestEventFactory.createEvent(member);
        EventFindConverter eventFindConverter = new EventFindConverter(new MemberConverter(), new GiftConverter());
        EventDto eventDto = eventFindConverter.convertToEventDto(event);

        given(eventRepository.findEventByCodeWithGift(event.getCode())).willReturn(Optional.of(event));
        given(this.eventFindConverter.convertToEventDto(event)).willReturn(eventDto);

        //when
        EventDto foundEventDto = eventService.findDetailEventByCode(event.getCode());

        //then
        Assertions.assertThat(foundEventDto.getCode()).isEqualTo(event.getCode());
        Assertions.assertThat(foundEventDto.getGifts().size()).isEqualTo(3);
        Assertions.assertThat(foundEventDto.getGifts().get(0).getGiftItems().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("멤버 아이디와 이벤트 아이디를 통해 삭제 테스트 - 성공")
    void deleteEvent() {
        Member member = mock(Member.class);
        Event event = TestEventFactory.createEvent(member);

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(eventRepository.findById(event.getId())).willReturn(Optional.of(event));

        Long deletedEventId = eventService.deleteEvent(member.getId(), event.getId());

        Assertions.assertThat(deletedEventId).isEqualTo(event.getId());
        Assertions.assertThat(event.getDeletedAt()).isNotNull();
    }
}
