package com.dokev.gold_dduck.gift.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.event.domain.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "gift", cascade = CascadeType.PERSIST)
    private final List<GiftItem> giftItems = new ArrayList<>();

    public Gift(String category, Integer itemCount) {
        Objects.requireNonNull(category, "선물 카테고리는 notnull이어야 합니다.");
        Objects.requireNonNull(itemCount, "선물 갯수는 notnull이어야 합니다.");

        this.category = category;
        this.itemCount = itemCount;
    }

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
