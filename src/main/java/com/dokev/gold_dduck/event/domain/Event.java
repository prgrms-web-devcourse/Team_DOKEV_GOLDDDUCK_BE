package com.dokev.gold_dduck.event.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.common.exception.EventClosedException;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "gift_choice_type", length = 20, nullable = false)
    private GiftChoiceType giftChoiceType;

    @Column(name = "start_at", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Column(name = "code", nullable = false, length = 16)
    private UUID code;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "progress_status", nullable = false, length = 30)
    private EventProgressStatus eventProgressStatus;

    @Column(name = "main_template", length = 30, nullable = false)
    private String mainTemplate;

    @Column(name = "max_participant_count", nullable = false)
    private Integer maxParticipantCount;

    @Column(name = "left_gift_count", nullable = false)
    private Integer leftGiftCount;

    @Column(name = "left_blank_count", nullable = false)
    private Integer leftBlankCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private final List<Gift> gifts = new ArrayList<>();

    @Builder(builderMethodName = "eventInternalBuilder")
    private Event(
        String title, GiftChoiceType giftChoiceType, LocalDateTime startAt, LocalDateTime endAt, UUID code,
        EventProgressStatus eventProgressStatus, String mainTemplate, Integer maxParticipantCount,
        Integer leftGiftCount, Integer leftBlankCount, Member member
    ) {
        this.title = title;
        this.giftChoiceType = giftChoiceType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.code = code;
        this.eventProgressStatus = eventProgressStatus;
        this.mainTemplate = mainTemplate;
        this.maxParticipantCount = maxParticipantCount;
        this.leftGiftCount = leftGiftCount;
        this.leftBlankCount = leftBlankCount;
        this.member = member;
    }

    public static EventBuilder builder(
        String title, GiftChoiceType giftChoiceType, LocalDateTime startAt, LocalDateTime endAt,
        EventProgressStatus eventProgressStatus, String mainTemplate, Integer maxParticipantCount,
        Integer leftGiftCount, Integer leftBlankCount, Member member
    ) {
        Objects.requireNonNull(giftChoiceType, "이벤트에서 선물 받을 방법을 선택해야 합니다.");
        Objects.requireNonNull(startAt, "이벤트 시작 시간은 notnull이어야 합니다.");
        Objects.requireNonNull(endAt, "이벤트 종료 시간은 notnull이어야 합니다.");
        Objects.requireNonNull(eventProgressStatus, "이벤트 상태는 notnull이어야 합니다.");
        Objects.requireNonNull(mainTemplate, "이벤트 대표 이미지 타입은 notnull이어야 합니다.");
        Objects.requireNonNull(maxParticipantCount, "이벤트 최대 참가자 수는 notnull이어야 합니다.");
        Objects.requireNonNull(leftBlankCount, "남은 꽝 개수는 notnull이어야 합니다.");
        Objects.requireNonNull(leftBlankCount, "남은 선물 개수는 notnull이어야 합니다.");
        Objects.requireNonNull(member, "이벤트 생성자는 notnull이어야 합니다.");

        return eventInternalBuilder()
            .title(title)
            .code(UUID.randomUUID())
            .giftChoiceType(giftChoiceType)
            .startAt(startAt)
            .endAt(endAt)
            .eventProgressStatus(eventProgressStatus)
            .mainTemplate(mainTemplate)
            .maxParticipantCount(maxParticipantCount)
            .leftGiftCount(leftGiftCount)
            .leftBlankCount(leftBlankCount)
            .member(member);
    }

    public void changeMember(Member member) {
        if (Objects.nonNull(this.member)) {
            this.member.getEvents().remove(this);
        }
        this.member = member;
        member.getEvents().add(this);
    }

    public void decreaseLeftGiftCount() {
        this.leftGiftCount--;
    }

    public void decreaseLeftBlankCount() {
        this.leftBlankCount--;
    }

    public void closeEvent() {
        this.eventProgressStatus = EventProgressStatus.CLOSED;
    }

    public void validateEndTime() {
        boolean eventEndTimeOver = this.endAt.isBefore(LocalDateTime.now());
        if (eventEndTimeOver) {
            closeEvent();
            throw new EventClosedException();
        }
    }

    public void validateCloseStatus() {
        if (this.eventProgressStatus == EventProgressStatus.CLOSED) {
            throw new EventClosedException();
        }
    }

    public void validateEventRunning() {
        validateCloseStatus();
        validateEndTime();
    }

    public void renewStatus() {
        if (this.eventProgressStatus == EventProgressStatus.CLOSED) {
            return;
        }
        if (this.giftChoiceType == GiftChoiceType.FIFO && this.leftGiftCount <= 0) {
            closeEvent();
            return;
        }
        if (this.giftChoiceType == GiftChoiceType.RANDOM && this.leftGiftCount + this.leftBlankCount <= 0) {
            closeEvent();
            return;
        }
        if (startAt.isEqual(LocalDateTime.now()) || (startAt.isBefore(LocalDateTime.now()) && endAt.isAfter(
            LocalDateTime.now()))) {
            eventProgressStatus = EventProgressStatus.RUNNING;
            return;
        }
        if (endAt.isEqual(LocalDateTime.now()) || endAt.isBefore(LocalDateTime.now())){
            eventProgressStatus = EventProgressStatus.CLOSED;
        }
    }

    public void deleteEvent() {
        this.deletedAt = LocalDateTime.now();
    }
}
