package com.dokev.gold_dduck.gift.dto;

import com.dokev.gold_dduck.common.util.TimeUtil;
import com.dokev.gold_dduck.gift.domain.GiftType;
import java.time.LocalDateTime;
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

    private LocalDateTime receivedDate;

    public GiftItemSearchDto(Long id, GiftType giftType, String content, boolean used, String category,
        String mainTemplate, String sender, LocalDateTime receivedDate) {
        this.id = id;
        this.giftType = giftType;
        this.content = content;
        this.used = used;
        this.category = category;
        this.mainTemplate = mainTemplate;
        this.sender = sender;
        this.receivedDate = TimeUtil.utcToSeoul(receivedDate);
    }
}
