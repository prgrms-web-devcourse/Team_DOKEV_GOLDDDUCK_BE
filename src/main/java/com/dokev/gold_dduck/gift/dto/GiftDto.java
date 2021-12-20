package com.dokev.gold_dduck.gift.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GiftDto {

    private Long id;

    private String category;

    private Integer itemCount;

    private boolean soldOut;

    private List<GiftItemDto> giftItems;

    public GiftDto(Long id, String category, Integer itemCount, boolean soldOut, List<GiftItemDto> giftItems) {
        this.id = id;
        this.category = category;
        this.itemCount = itemCount;
        this.soldOut = soldOut;
        this.giftItems = giftItems;
    }
}
