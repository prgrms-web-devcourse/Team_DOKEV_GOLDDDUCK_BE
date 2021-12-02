package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class EventResponse {

    private Long eventId;

    private String eventType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private UUID code;

    private EventProgressStatus eventProgressStatus;

    private String mainTemplate;

    private Integer maxParticipantCount;

    private Member member;

    private List<Gift> gifts = new ArrayList<>();

    public EventResponse(Long eventId, String eventType, LocalDateTime startAt, LocalDateTime endAt,
            UUID code, EventProgressStatus eventProgressStatus, String mainTemplate, Integer maxParticipantCount,
            Member member, List<Gift> gifts) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.code = code;
        this.eventProgressStatus = eventProgressStatus;
        this.mainTemplate = mainTemplate;
        this.maxParticipantCount = maxParticipantCount;
        this.member = member;
        this.gifts = gifts;
    }
}
