package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.gift.converter.GiftConverter;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.dto.GiftResponseDto;
import com.dokev.gold_dduck.member.converter.MemberConverter;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    private final MemberConverter memberConverter;
    private final GiftConverter giftConverter;

    public EventConverter(MemberConverter memberConverter, GiftConverter giftConverter) {
        this.memberConverter = memberConverter;
        this.giftConverter = giftConverter;
    }

    public EventResponseDto convertToApiResponse(Event event) {
        Member member = event.getMember();
        MemberResponseDto memberResponseDto = memberConverter.convertToMemberResponseDto(member);

        List<Gift> gifts = event.getGifts();
        List<GiftResponseDto> giftDtos = gifts.stream().map(giftConverter::convertToGiftResponseDto)
                .collect(Collectors.toList());

        return new EventResponseDto(event.getId(), event.getEventType(), event.getStartAt(), event.getEndAt(),
                event.getCode(),
                event.getEventProgressStatus().toString(), event.getMainTemplate(), event.getMaxParticipantCount(),
                memberResponseDto, giftDtos);
    }


}
