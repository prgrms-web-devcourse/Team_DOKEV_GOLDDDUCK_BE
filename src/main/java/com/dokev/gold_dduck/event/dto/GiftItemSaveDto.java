package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.gift.domain.GiftType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class GiftItemSaveDto {

    @NotNull
    private GiftType giftType;

    @NotNull
    private String content;
}
