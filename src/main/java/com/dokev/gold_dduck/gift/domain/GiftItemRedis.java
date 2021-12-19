package com.dokev.gold_dduck.gift.domain;

import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash("gift_item")
public class GiftItemRedis {

    @Id
    private Long id;

    private Long giftItemId;

    private GiftType giftType;

    private String content;

    private boolean used;

    private Long giftId;

    private String category;

    private String mainTemplate;

    private String sender;

    public GiftItemRedis(GiftType giftType, String content, boolean used, Long giftId, String category,
        String mainTemplate, String sender) {
        this.giftType = giftType;
        this.content = content;
        this.used = used;
        this.giftId = giftId;
        this.category = category;
        this.mainTemplate = mainTemplate;
        this.sender = sender;
    }

    public GiftItemRedis(GiftItem giftItem) {
        this.giftItemId = giftItem.getId();
        this.giftType = giftItem.getGiftType();
        this.content = giftItem.getContent();
        this.used = giftItem.isUsed();
        this.giftId = giftItem.getGift().getId();
        this.category = giftItem.getGift().getCategory();
        this.mainTemplate = mainTemplate;
        this.sender = sender;
    }

}
