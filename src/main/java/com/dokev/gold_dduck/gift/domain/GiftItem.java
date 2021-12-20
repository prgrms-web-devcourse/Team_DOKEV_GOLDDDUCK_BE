package com.dokev.gold_dduck.gift.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.common.exception.GiftAlreadyAllocatedException;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.member.domain.Member;
import java.util.Objects;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@ToString(of = {"id", "giftType", "content", "used"})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id")
    private Gift gift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public GiftItem(GiftType giftType, String content, boolean used) {
        Objects.requireNonNull(giftType, "선물 유형은 notnull이어야 합니다.");
        Objects.requireNonNull(content, "선물 내용은 notnull이어야 합니다.");

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

    public void allocateMember(Member member) {
        Objects.requireNonNull(member, "member는 notnull이어야 합니다.");
        if (Objects.nonNull(this.member)) {
            throw new GiftAlreadyAllocatedException(this.id, this.member.getId());
        }
        this.member = member;
    }

    public void changeUsed(boolean used) {
        this.used = used;
    }
}
