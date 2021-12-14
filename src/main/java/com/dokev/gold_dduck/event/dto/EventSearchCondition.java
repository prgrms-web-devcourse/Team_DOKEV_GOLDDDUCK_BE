package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import lombok.Getter;

@Getter
public class EventSearchCondition {

    private final EventProgressStatus eventProgressStatus;

    public EventSearchCondition(EventProgressStatus eventProgressStatus) {
        this.eventProgressStatus = eventProgressStatus;
    }
}
