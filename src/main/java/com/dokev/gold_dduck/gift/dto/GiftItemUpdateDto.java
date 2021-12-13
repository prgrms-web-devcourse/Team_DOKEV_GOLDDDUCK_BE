package com.dokev.gold_dduck.gift.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GiftItemUpdateDto {

    @NotNull
    private Boolean used;

    public GiftItemUpdateDto(Boolean used) {
        this.used = used;
    }
}
