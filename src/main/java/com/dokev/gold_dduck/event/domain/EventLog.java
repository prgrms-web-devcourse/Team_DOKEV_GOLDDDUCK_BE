package com.dokev.gold_dduck.event.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.member.domain.Member;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "event_log")
@Entity
public class EventLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id")
    private Gift gift;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_item_id")
    private GiftItem giftItem;

    public EventLog(Event event, Member member, Gift gift, GiftItem giftItem) {
        Objects.requireNonNull(event, "event는 notnull이어야 합니다.");
        Objects.requireNonNull(member, "member는 notnull이어야 합니다.");

        this.event = event;
        this.member = member;
        this.gift = gift;
        this.giftItem = giftItem;
    }
}
