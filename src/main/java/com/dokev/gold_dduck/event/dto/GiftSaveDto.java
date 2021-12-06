package com.dokev.gold_dduck.event.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GiftSaveDto {

    private String category;
    private List<GiftItemSaveDto> giftItems;
}
