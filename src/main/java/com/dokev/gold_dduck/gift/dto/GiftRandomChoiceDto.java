package com.dokev.gold_dduck.gift.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GiftRandomChoiceDto {

    @NotNull
    private Long eventId;

    @NotNull
    private Long memberId;

    public GiftRandomChoiceDto(Long eventId, Long memberId) {
        this.eventId = eventId;
        this.memberId = memberId;
    }
}
