package com.dokev.gold_dduck.gift.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.event.domain.Event;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@Table(name = "gift")
public class Gift extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Long id;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "item_count")
    private Integer itemCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "gift")
    private List<GiftItem> giftItems = new ArrayList<>();

    public Optional<Integer> getItemCount() {
        return Optional.ofNullable(itemCount);
    }

    public void changeEvent(Event event) {
        if (Objects.nonNull(this.event)) {
            this.event.getGifts().remove(this);
        }
        this.event = event;
        event.getGifts().add(this);
    }
}
