package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.member.dto.MemberDto;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventLogDto {

    @NotNull
    private String category;

    private List<MemberDto> winners;

    public EventLogDto(String category, List<MemberDto> winners) {
        this.category = category;
        this.winners = winners;
    }
}
