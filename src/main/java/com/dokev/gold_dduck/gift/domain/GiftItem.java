package com.dokev.gold_dduck.gift.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "gift_item")
public class GiftItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_item_id")
    private Long id;

    @Column(name = "gift_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private GiftType giftType;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "used", nullable = false)
    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "gift_id")
    private Gift gift;

    public GiftItem(GiftType giftType, String content, boolean used) {
        Objects.requireNonNull(giftType, "선물 유형은 notnull이어야 합니다.");
        Objects.requireNonNull(content, "선물 내용은 notnull이어야 합니다.");
        Objects.requireNonNull(used, "선물 아이템 사용여부는 notnull이어야 합니다.");

        this.giftType = giftType;
        this.content = content;
        this.used = used;
    }

    public void changeGift(Gift gift) {
        if (Objects.nonNull(this.gift)) {
            this.gift.getGiftItems().remove(this);
        }
        this.gift = gift;
        gift.getGiftItems().add(this);
    }
}
