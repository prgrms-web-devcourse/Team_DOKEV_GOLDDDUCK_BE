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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class EventFindConverter {

    private MemberConverter memberConverter;
    private GiftConverter giftConverter;

    public EventFindConverter(MemberConverter memberConverter, GiftConverter giftConverter) {
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
