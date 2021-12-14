package com.dokev.gold_dduck.gift.dto;

import lombok.Getter;

@Getter
public class GiftItemSearchCondition {

    private final Boolean used;

    public GiftItemSearchCondition(Boolean used) {
        this.used = used;
    }
}
