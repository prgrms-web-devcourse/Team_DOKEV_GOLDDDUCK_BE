package com.dokev.gold_dduck.gift.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GiftDto {

    private Long id;

    private String category;

    private Integer itemCount;

    private List<GiftItemDto> giftItems = new ArrayList<>();

    public GiftDto(Long id, String category, Integer itemCount, List<GiftItemDto> giftItems) {
        this.id = id;
        this.category = category;
        this.itemCount = itemCount;
        this.giftItems = giftItems;
    }
}