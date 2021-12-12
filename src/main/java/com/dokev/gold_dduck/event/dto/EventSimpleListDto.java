package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.common.PaginationDto;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class EventSimpleListDto {

    private List<EventSimpleDto> simpleEventList;

    private PaginationDto pagination;

    public EventSimpleListDto(Page<EventSimpleDto> page) {
        this.simpleEventList = page.getContent();
        this.pagination = new PaginationDto(page);
    }
}
