package com.dokev.gold_dduck.gift.dto;

import com.dokev.gold_dduck.gift.domain.GiftType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class GiftItemSearchDto {

    private Long id;

    private GiftType giftType;

    private String content;

    private boolean used;

    private String category;

    private String mainTemplate;

    private String sender;

    private String receivedDate;

    public GiftItemSearchDto(Long id, GiftType giftType, String content, boolean used, String category,
        String mainTemplate, String sender, String receivedDate) {
        this.id = id;
        this.giftType = giftType;
        this.content = content;
        this.used = used;
        this.category = category;
        this.mainTemplate = mainTemplate;
        this.sender = sender;
        this.receivedDate = receivedDate;
    }
}
