package com.dokev.gold_dduck.event.service;

import com.dokev.gold_dduck.event.converter.EventLogConverter;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.dto.EventLogDto;
import com.dokev.gold_dduck.event.repository.EventLogRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class EventLogService {

    private final EventLogRepository eventLogRepository;
    private final EventLogConverter eventLogConverter;

    public EventLogService(EventLogRepository eventLogRepository,
        EventLogConverter eventLogConverter) {
        this.eventLogRepository = eventLogRepository;
        this.eventLogConverter = eventLogConverter;
    }

    public List<EventLogDto> findEventWinnerLogs(Long eventId) {
        List<EventLog> eventLogs = eventLogRepository.findWinnerLogsByEventId(eventId);
        return eventLogConverter.convertToEventLogDtos(eventLogs);
    }
}
