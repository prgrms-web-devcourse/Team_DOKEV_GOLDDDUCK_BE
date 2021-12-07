package com.dokev.gold_dduck.gift.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GiftFifoChoiceDto {

    @NotNull
    private Long eventId;

    @NotNull
    private Long memberId;

    @NotNull
    private Long giftId;

    public GiftFifoChoiceDto(Long eventId, Long memberId, Long giftId) {
        this.eventId = eventId;
        this.memberId = memberId;
        this.giftId = giftId;
    }
}
