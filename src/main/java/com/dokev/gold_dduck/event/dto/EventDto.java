package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.common.util.TimeUtil;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
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

    private String title;

    private GiftChoiceType giftChoiceType;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime startAt;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime endAt;

    private UUID code;

    private EventProgressStatus eventProgressStatus;

    private String mainTemplate;

    private Integer maxParticipantCount;

    private MemberDto member;

    private List<GiftDto> gifts;

    public EventDto(Long eventId, String title, GiftChoiceType giftChoiceType, LocalDateTime startAt,
        LocalDateTime endAt,
        UUID code, EventProgressStatus eventProgressStatus, String mainTemplate, Integer maxParticipantCount,
        MemberDto member, List<GiftDto> gifts) {
        this.eventId = eventId;
        this.title = title;
        this.giftChoiceType = giftChoiceType;
        this.startAt = TimeUtil.utcToSeoul(startAt);
        this.endAt = TimeUtil.utcToSeoul(endAt);
        this.code = code;
        this.eventProgressStatus = eventProgressStatus;
        this.mainTemplate = mainTemplate;
        this.maxParticipantCount = maxParticipantCount;
        this.member = member;
        this.gifts = gifts;
    }
}
