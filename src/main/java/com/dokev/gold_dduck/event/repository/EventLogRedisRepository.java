package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.EventLogRedis;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EventLogRedisRepository extends CrudRepository<EventLogRedis, Long> {

    List<EventLogRedis> findAllByEventId(Long eventId);

    boolean existsByEventIdAndMemberId(Long eventId, Long memberId);
}
