package com.dokev.gold_dduck.gift.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class GiftItemSimpleDto {

    private Long giftItemId;

    private Long giftId;

    public GiftItemSimpleDto(Long giftItemId, Long giftId) {
        this.giftItemId = giftItemId;
        this.giftId = giftId;
    }
}
