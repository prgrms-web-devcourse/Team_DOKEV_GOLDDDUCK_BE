package com.dokev.gold_dduck.event.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "gift_choice_type", length = 20, nullable = false)
    private GiftChoiceType giftChoiceType;

    @Column(name = "start_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime startAt;

    @Column(name = "end_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime endAt;

    @Column(name = "code", nullable = false)
    private UUID code;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "progress_status", nullable = false, length = 30)
    private EventProgressStatus eventProgressStatus;

    @Column(name = "main_template", length = 30, nullable = false)
    private String mainTemplate;

    @Column(name = "max_participant_count", nullable = false)
    private Integer maxParticipantCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "event")
    private List<Gift> gifts = new ArrayList<>();

    @Builder(builderMethodName = "eventInternalBuilder")
    private Event(GiftChoiceType giftChoiceType, LocalDateTime startAt, LocalDateTime endAt, UUID code,
        EventProgressStatus eventProgressStatus, String mainTemplate, Integer maxParticipantCount,
        Member member) {
        this.giftChoiceType = giftChoiceType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.code = code;
        this.eventProgressStatus = eventProgressStatus;
        this.mainTemplate = mainTemplate;
        this.maxParticipantCount = maxParticipantCount;
        this.member = member;
    }

    public static EventBuilder builder(GiftChoiceType giftChoiceType, LocalDateTime startAt, LocalDateTime endAt,
        EventProgressStatus eventProgressStatus, String mainTemplate, Integer maxParticipantCount, Member member) {
        return eventInternalBuilder()
            .giftChoiceType(giftChoiceType)
            .startAt(startAt)
            .endAt(endAt)
            .eventProgressStatus(eventProgressStatus)
            .mainTemplate(mainTemplate)
            .maxParticipantCount(maxParticipantCount)
            .member(member);
    }

    public void changeMember(Member member) {
        if (Objects.nonNull(this.member)) {
            this.member.getEvents().remove(this);
        }
        this.member = member;
        member.getEvents().add(this);
    }
}
