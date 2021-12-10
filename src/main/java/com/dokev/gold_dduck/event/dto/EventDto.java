package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.member.dto.MemberDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventDto {

    private Long eventId;

    private GiftChoiceType giftChoiceType;

    @JsonFormat(shape = Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    @JsonFormat(shape = Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    private UUID code;

    private String eventProgressStatus;

    private String mainTemplate;

    private Integer maxParticipantCount;

    private MemberDto member;

    private List<GiftDto> gifts;

    public EventDto(Long eventId, GiftChoiceType giftChoiceType, LocalDateTime startAt, LocalDateTime endAt,
        UUID code, String eventProgressStatus, String mainTemplate, Integer maxParticipantCount,
        MemberDto member, List<GiftDto> gifts) {
        this.eventId = eventId;
        this.giftChoiceType = giftChoiceType;
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
