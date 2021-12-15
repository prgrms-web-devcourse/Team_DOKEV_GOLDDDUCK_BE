package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EventSaveDto {

    @NotNull
    private Long memberId;

    @NotNull
    private String title;

    @NotNull
    @Size(min = 1, max = 100)
    private List<GiftSaveDto> gifts;

    @NotNull
    private GiftChoiceType giftChoiceType;

    @NotNull
    private String mainTemplate;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime startAt;

    @NotNull
    @Future
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime endAt;

    @NotNull
    private int maxParticipantCount;

    public void changedMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
