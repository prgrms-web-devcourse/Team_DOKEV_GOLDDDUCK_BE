package com.dokev.gold_dduck.gift.dto;

import com.dokev.gold_dduck.gift.domain.GiftType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class GiftItemDetailDto {

    private Long id;

    private GiftType giftType;

    private String content;

    private boolean used;

    private Long giftId;

    private String category;

    private String mainTemplate;

    private String sender;

    public GiftItemDetailDto(Long id, GiftType giftType, String content, boolean used, Long giftId,
        String category,
        String mainTemplate, String sender) {
        this.id = id;
        this.giftType = giftType;
        this.content = content;
        this.used = used;
        this.giftId = giftId;
        this.category = category;
        this.mainTemplate = mainTemplate;
        this.sender = sender;
    }
}
