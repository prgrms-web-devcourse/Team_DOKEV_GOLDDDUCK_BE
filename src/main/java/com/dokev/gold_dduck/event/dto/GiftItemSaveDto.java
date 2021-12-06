package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.gift.domain.GiftType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GiftItemSaveDto {

    private GiftType giftType;
    private String content;
    // private MultipartFile file;
}
