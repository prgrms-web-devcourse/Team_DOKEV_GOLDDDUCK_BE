package com.dokev.gold_dduck.gift;

import com.dokev.gold_dduck.common.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "gift_item")
public class GiftItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_item_id")
    private Long id;

    @Column(name = "gift_type", length = 20)
    @Enumerated(EnumType.STRING)
    private GiftType giftType;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "used", nullable = false)
    private Boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id")
    private Gift gift;
}
