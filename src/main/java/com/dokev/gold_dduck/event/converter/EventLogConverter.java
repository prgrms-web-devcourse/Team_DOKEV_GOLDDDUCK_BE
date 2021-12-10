package com.dokev.gold_dduck.event.converter;

import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.dto.EventLogDto;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.member.domain.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class EventLogConverter {

    public List<EventLogDto> convertToEventLogDtos(List<EventLog> eventLogs) {
        List<EventLogDto> eventLogDtos = new ArrayList<>();
        Map<Gift, List<EventLog>> giftListMap = makeGroup(eventLogs);
        for (int i = 0; i < giftListMap.size(); i++) {
            List<EventLog> groupedEventLogs = giftListMap.get(i);
            eventLogDtos.add(convertToEventLogDto(groupedEventLogs));
        }
        return eventLogDtos;
    }

    private Map<Gift, List<EventLog>> makeGroup(List<EventLog> eventLogs) {
        if (eventLogs.isEmpty()) {
            return new HashMap<>();
        }
        return eventLogs.stream()
            .collect(Collectors.groupingBy(EventLog::getGift));
    }

    private EventLogDto convertToEventLogDto(List<EventLog> eventLogs) {
        List<Member> members = new ArrayList<>();

        for (EventLog eventLog : eventLogs) {
            members.add(eventLog.getMember());
        }
        return new EventLogDto(eventLogs.get(0).getGift().getCategory(), members);
    }

}
