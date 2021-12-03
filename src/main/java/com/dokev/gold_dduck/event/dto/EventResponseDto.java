package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.gift.dto.GiftResponseDto;
import com.dokev.gold_dduck.member.dto.MemberResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class EventResponseDto {

    private Long eventId;

    private String eventType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private UUID code;

    private String eventProgressStatus;

    private String mainTemplate;

    private Integer maxParticipantCount;

    private MemberResponseDto member;

    private List<GiftResponseDto> gifts = new ArrayList<>();

    public EventResponseDto(Long eventId, String eventType, LocalDateTime startAt, LocalDateTime endAt,
            UUID code, String eventProgressStatus, String mainTemplate, Integer maxParticipantCount,
            MemberResponseDto member, List<GiftResponseDto> gifts) {
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
