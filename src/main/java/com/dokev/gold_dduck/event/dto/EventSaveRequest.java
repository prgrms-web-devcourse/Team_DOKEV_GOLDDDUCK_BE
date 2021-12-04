package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventSaveRequest {

    private Long memberId;
    private List<GiftSaveDto> gifts;
    private GiftChoiceType giftChoiceType;
    private String mainTemplate;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int maxParticipantCount;
}
