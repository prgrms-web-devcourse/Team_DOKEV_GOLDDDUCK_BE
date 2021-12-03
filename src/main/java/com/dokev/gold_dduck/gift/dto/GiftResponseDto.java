package com.dokev.gold_dduck.gift.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GiftResponseDto {

    private Long id;

    private String category;

    private Integer itemCount;

    private List<GiftItemResponseDto> giftItems = new ArrayList<>();

    public GiftResponseDto(Long id, String category, Integer itemCount, List<GiftItemResponseDto> giftItems) {
        this.id = id;
        this.category = category;
        this.itemCount = itemCount;
        this.giftItems = giftItems;
    }
}
