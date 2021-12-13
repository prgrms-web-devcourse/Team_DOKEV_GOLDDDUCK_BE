package com.dokev.gold_dduck.event.converter;

import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.dto.EventLogDto;
import com.dokev.gold_dduck.member.converter.MemberConverter;
import com.dokev.gold_dduck.member.dto.MemberDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class EventLogConverter {

    private MemberConverter memberConverter;

    @Autowired
    public EventLogConverter(MemberConverter memberConverter) {
        this.memberConverter = memberConverter;
    }

    public List<EventLogDto> convertToEventLogDtos(List<EventLog> eventLogs) {
        List<EventLogDto> eventLogDtos = new ArrayList<>();
        Map<String, List<EventLog>> giftListMap = makeGroup(eventLogs);

        for (String category : giftListMap.keySet()) {
            List<EventLog> groupedEventLogs = giftListMap.get(category);
            eventLogDtos.add(convertToEventLogDto(groupedEventLogs));
        }

        return eventLogDtos;
    }

    private Map<String, List<EventLog>> makeGroup(List<EventLog> eventLogs) {
        if (eventLogs.isEmpty()) {
            return new HashMap<>();
        }

        return eventLogs.stream()
            .collect(Collectors.groupingBy(eventLog -> eventLog.getGift().getCategory()));
    }

    private EventLogDto convertToEventLogDto(List<EventLog> eventLogs) {
        List<MemberDto> members = new ArrayList<>();

        for (EventLog eventLog : eventLogs) {
            MemberDto memberDto = memberConverter.convertToMemberDto(eventLog.getMember());
            members.add(memberDto);
        }
        return new EventLogDto(eventLogs.get(0).getGift().getCategory(), members);
    }

}
