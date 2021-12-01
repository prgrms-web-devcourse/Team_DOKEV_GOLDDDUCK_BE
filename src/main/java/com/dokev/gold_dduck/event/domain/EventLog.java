package com.dokev.gold_dduck.event.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;

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
}
