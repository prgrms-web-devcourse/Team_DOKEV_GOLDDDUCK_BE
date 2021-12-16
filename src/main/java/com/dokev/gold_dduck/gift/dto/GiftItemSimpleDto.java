package com.dokev.gold_dduck.gift.dto;

import com.dokev.gold_dduck.gift.domain.GiftType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class GiftItemSimpleDto {

    private Long giftItemId;

    public GiftItemSimpleDto(Long giftItemId) {
        this.giftItemId = giftItemId;
    }
}
