package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class EventSimpleDto {

    private final Long eventId;

    private final String title;

    private final GiftChoiceType giftChoiceType;

    @JsonFormat(shape = Shape.STRING, timezone = "Asia/Seoul")
    private final LocalDateTime startAt;

    @JsonFormat(shape = Shape.STRING, timezone = "Asia/Seoul")
    private final LocalDateTime endAt;

    private final UUID code;

    private final EventProgressStatus eventProgressStatus;

    private final String mainTemplate;

    private final Integer maxParticipantCount;

    @JsonFormat(shape = Shape.STRING, timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public EventSimpleDto(Long eventId, String title, GiftChoiceType giftChoiceType, LocalDateTime startAt,
        LocalDateTime endAt, UUID code, EventProgressStatus eventProgressStatus, String mainTemplate,
        Integer maxParticipantCount, LocalDateTime createdAt) {
        this.eventId = eventId;
        this.title = title;
        this.giftChoiceType = giftChoiceType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.code = code;
        this.eventProgressStatus = eventProgressStatus;
        this.mainTemplate = mainTemplate;
        this.maxParticipantCount = maxParticipantCount;
        this.createdAt = createdAt;
    }
}
