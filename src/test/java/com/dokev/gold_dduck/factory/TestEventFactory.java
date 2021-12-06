package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.Event.EventBuilder;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.member.domain.Member;
import java.time.LocalDateTime;

public class TestEventFactory {

    public static EventBuilder builder(Member member) {
        return Event.builder(
            GiftChoiceType.FIFO,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(10),
            EventProgressStatus.RUNNING,
            "template1",
            60,
            member);
    }

    public static EventBuilder dtoUseBuilder(EventSaveDto eventSaveRequest, Member member) {
        return Event.builder(
            eventSaveRequest.getGiftChoiceType(),
            eventSaveRequest.getStartAt(),
            eventSaveRequest.getEndAt(),
            EventProgressStatus.RUNNING,
            eventSaveRequest.getMainTemplate(),
            eventSaveRequest.getMaxParticipantCount(),
            member);
    }
}
