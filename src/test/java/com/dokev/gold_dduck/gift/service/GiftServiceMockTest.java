package com.dokev.gold_dduck.gift.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.dokev.gold_dduck.common.exception.EntityNotFoundException;
import com.dokev.gold_dduck.common.exception.EventAlreadyParticipatedException;
import com.dokev.gold_dduck.common.exception.GiftStockOutException;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.repository.EventLogRepository;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.gift.repository.GiftItemRepository;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GiftServiceMockTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private GiftRepository giftRepository;

    @Mock
    private GiftItemRepository giftItemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EventLogRepository eventLogRepository;

    @InjectMocks
    private GiftService sut;

    @Test
    @DisplayName("선착순으로 선물 받기 성공 테스트")
    void chooseGiftItemByFIFOSuccessTest() {
        //given
        Event mockEvent = mock(Event.class);
        Member mockMember = mock(Member.class);
        Gift mockGift = mock(Gift.class);
        GiftItem targetGiftItem = new GiftItem(GiftType.IMAGE, "coffee", false);
        targetGiftItem.changeGift(mockGift);
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(mockEvent));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(mockMember));
        given(eventLogRepository.existsByEventIdAndMemberId(anyLong(), anyLong())).willReturn(false);
        given(giftRepository.findById(anyLong())).willReturn(Optional.of(mockGift));
        given(giftItemRepository.findByGiftIdWithPageForUpdate(anyLong(), any())).willReturn(List.of(targetGiftItem));
        //when
        GiftItemDto giftItemDto = sut.chooseGiftItemByFIFO(1L, 1L, 1L);
        //then
        ArgumentCaptor<EventLog> eventLogArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        then(eventLogRepository).should().save(eventLogArgumentCaptor.capture());
        EventLog eventLog = eventLogArgumentCaptor.getValue();
        assertThat(eventLog.getEvent(), is(mockEvent));
        assertThat(eventLog.getMember(), is(mockMember));
        assertThat(eventLog.getGift(), is(mockGift));
        assertThat(eventLog.getGiftItem(), is(targetGiftItem));
        assertThat(targetGiftItem.getMember(), is(mockMember));
        assertThat(giftItemDto.getGiftType(), is(targetGiftItem.getGiftType()));
        assertThat(giftItemDto.getContent(), is(targetGiftItem.getContent()));
        assertThat(giftItemDto.getId(), is(targetGiftItem.getId()));
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (잘못된 eventId)")
    void chooseGiftItemByFIFOFailureTest() {
        //given
        Long invalidEventId = 1L;
        given(eventRepository.findById(invalidEventId)).willReturn(Optional.empty());
        //when then
        assertThatThrownBy(
            () -> sut.chooseGiftItemByFIFO(invalidEventId, 1L, 1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining(Event.class.getName());
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (잘못된 memberId)")
    void chooseGiftItemByFIFOFailureTest2() {
        //given
        Event mockEvent = mock(Event.class);
        Long invalidMemberId = 1L;
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(mockEvent));
        given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());
        //when then
        assertThatThrownBy(
            () -> sut.chooseGiftItemByFIFO(1L, invalidMemberId, 1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining(Member.class.getName());
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (이미 이벤트에 참여한 멤버)")
    void chooseGiftItemByFIFOFailureTest3() {
        //given
        Event mockEvent = mock(Event.class);
        Member mockMember = mock(Member.class);
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(mockEvent));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(mockMember));
        given(eventLogRepository.existsByEventIdAndMemberId(anyLong(), anyLong())).willReturn(true);
        //when then
        assertThatThrownBy(
            () -> sut.chooseGiftItemByFIFO(1L, 1L, 1L))
            .isInstanceOf(EventAlreadyParticipatedException.class);
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (잘못된 giftId)")
    void chooseGiftItemByFIFOFailureTest4() {
        //given
        Event mockEvent = mock(Event.class);
        Member mockMember = mock(Member.class);
        Long invalidGiftId = 1L;
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(mockEvent));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(mockMember));
        given(eventLogRepository.existsByEventIdAndMemberId(anyLong(), anyLong())).willReturn(false);
        given(giftRepository.findById(invalidGiftId)).willReturn(Optional.empty());
        //when then
        assertThatThrownBy(
            () -> sut.chooseGiftItemByFIFO(1L, 1L, invalidGiftId))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (선물 재고 부족)")
    void chooseGiftItemByFIFOFailureTest5() {
        //given
        Event mockEvent = mock(Event.class);
        Member mockMember = mock(Member.class);
        Gift mockGift = mock(Gift.class);
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(mockEvent));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(mockMember));
        given(eventLogRepository.existsByEventIdAndMemberId(anyLong(), anyLong())).willReturn(false);
        given(giftRepository.findById(anyLong())).willReturn(Optional.of(mockGift));
        given(giftItemRepository.findByGiftIdWithPageForUpdate(anyLong(), any())).willReturn(new ArrayList<>());
        //when then
        assertThatThrownBy(
            () -> sut.chooseGiftItemByFIFO(1L, 1L, 1L))
            .isInstanceOf(GiftStockOutException.class);
    }
}
