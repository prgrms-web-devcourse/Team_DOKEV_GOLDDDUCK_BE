package com.dokev.gold_dduck.event.controller;

import com.dokev.gold_dduck.common.ApiResponse;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.service.EventService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api")
@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/v1/events")
    public ApiResponse<UUID> saveEvent(@RequestBody @Validated EventSaveDto eventSaveDto) {
        UUID eventCode = eventService.saveEvent(eventSaveDto);
        return ApiResponse.success(eventCode);
    }
}
