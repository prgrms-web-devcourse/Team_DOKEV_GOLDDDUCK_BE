package com.dokev.gold_dduck.gift.dto;

import lombok.Getter;

@Getter
public class GiftItemResponseDto {

    private Long id;

    private String giftType;

    private String content;

    private boolean used;

    public GiftItemResponseDto(Long id, String giftType, String content, boolean used) {
        this.id = id;
        this.giftType = giftType;
        this.content = content;
        this.used = used;
    }
}
