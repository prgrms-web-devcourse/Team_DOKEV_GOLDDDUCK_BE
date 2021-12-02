package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.common.ApiResponse;
import com.dokev.gold_dduck.event.domain.Event;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    public ApiResponse<EventResponse> convertToApiResponse(Event event) {
        EventResponse eventResponse = new EventResponse(event.getId(), event.getEventType(), event.getStartAt(),
                event.getEndAt(), event.getCode(),
                event.getEventProgressStatus(), event.getMainTemplate(), event.getMaxParticipantCount(),
                event.getMember(), event.getGifts());

        return ApiResponse.success(eventResponse);
    }


}
