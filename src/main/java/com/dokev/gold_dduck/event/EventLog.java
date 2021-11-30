package com.dokev.gold_dduck.event;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.gift.Gift;
import com.dokev.gold_dduck.gift.GiftItem;
import com.dokev.gold_dduck.member.Member;
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
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id")
    private Gift gift;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_item_id")
    private GiftItem giftItem;
}
