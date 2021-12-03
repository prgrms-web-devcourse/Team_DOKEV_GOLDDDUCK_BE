package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.gift.converter.GiftConverter;
import com.dokev.gold_dduck.gift.dto.GiftResponseDto;
import com.dokev.gold_dduck.member.converter.MemberConverter;
import com.dokev.gold_dduck.member.dto.MemberResponseDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    private final MemberConverter memberConverter;
    private final GiftConverter giftConverter;

    public EventConverter(MemberConverter memberConverter, GiftConverter giftConverter) {
        this.memberConverter = memberConverter;
        this.giftConverter = giftConverter;
    }

    public EventResponseDto convertToEventResponseDto(Event event, List<GiftResponseDto> giftResponseDtos,
            MemberResponseDto memberResponseDto) {

        return new EventResponseDto(event.getId(), event.getGiftChoiceType(), event.getStartAt(), event.getEndAt(),
                event.getCode(), event.getEventProgressStatus().toString(), event.getMainTemplate(),
                event.getMaxParticipantCount(), memberResponseDto, giftResponseDtos);
    }


}
