package com.dokev.gold_dduck.event.controller;

import com.dokev.gold_dduck.common.ApiResponse;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.event.dto.EventLogDto;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.dto.EventSearchCondition;
import com.dokev.gold_dduck.event.dto.EventSimpleListDto;
import com.dokev.gold_dduck.event.service.EventLogService;
import com.dokev.gold_dduck.event.service.EventService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api")
@RestController
public class EventController {

    private final EventService eventService;
    private final EventLogService eventLogService;

    @Autowired
    public EventController(EventService eventService,
        EventLogService eventLogService) {
        this.eventService = eventService;
        this.eventLogService = eventLogService;
    }

    @PostMapping(value = "/v1/events")
    public ApiResponse<UUID> saveEvent(@ModelAttribute(value = "eventSaveDto") @Validated EventSaveDto eventSaveDto) {
        UUID eventCode = eventService.saveEvent(eventSaveDto);
        return ApiResponse.success(eventCode);
    }

    @GetMapping(value = "/v1/events/{event-code}")
    public ApiResponse<EventDto> findEventByCode(@PathVariable("event-code") UUID eventCode) {
        EventDto eventDto = eventService.findDetailEventByCode(eventCode);
        return ApiResponse.success(eventDto);
    }

    @GetMapping(value = "/v1/members/{memberId}/events")
    public ApiResponse<EventSimpleListDto> searchSimpleDescByMember(
        @PathVariable Long memberId,
        @RequestParam(required = false) EventProgressStatus eventProgressStatus,
        @PageableDefault(size = 4) Pageable pageable
    ) {
        EventSearchCondition eventSearchCondition = new EventSearchCondition(eventProgressStatus);
        return ApiResponse.success(eventService.searchSimpleDescByMember(memberId, eventSearchCondition, pageable));
    }

    @DeleteMapping(value = "/v1/members/{memberId}/events/{eventId}")
    public ApiResponse<Long> deleteEvent(@PathVariable Long memberId, @PathVariable Long eventId) {
        return ApiResponse.success(eventService.deleteEvent(memberId, eventId));
    }

    @GetMapping(value = "/v1/members/{memberId}/{eventId}/winners")
    public ApiResponse<List<EventLogDto>> searchWinners(@PathVariable Long memberId, @PathVariable Long eventId) {
        return ApiResponse.success(eventLogService.findEventWinnerLogs(eventId));
    }
}
