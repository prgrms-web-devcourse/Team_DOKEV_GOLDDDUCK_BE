package com.dokev.gold_dduck.event.converter;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.gift.converter.GiftConverter;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.member.converter.MemberConverter;
import com.dokev.gold_dduck.member.dto.MemberDto;
import java.util.ArrayList;
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

    public EventDto convertToEventDto(Event event) {
        MemberDto memberDto = memberConverter.convertToMemberDto(event.getMember());

        List<GiftDto> giftDtos = new ArrayList<>();
        for (Gift gift : event.getGifts()) {
            giftDtos.add(giftConverter.convertToGiftDto(gift));
        }

        return new EventDto(event.getId(), event.getGiftChoiceType(), event.getStartAt(), event.getEndAt(),
                event.getCode(), event.getEventProgressStatus().toString(), event.getMainTemplate(),
                event.getMaxParticipantCount(), memberDto, giftDtos);
    }
}
