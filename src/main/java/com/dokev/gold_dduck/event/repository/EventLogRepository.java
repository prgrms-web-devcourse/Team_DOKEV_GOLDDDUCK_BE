package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.EventLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    @Query("select case when count(el) > 0 then true else false end"
        + " from EventLog el"
        + " where el.event.id = :eventId and el.member.id = :memberId")
    boolean existsByEventIdAndMemberId(@Param("eventId") Long eventId, @Param("memberId") Long memberId);

    @Query("SELECT el"
        + " FROM EventLog el"
        + " WHERE el.event.id = :eventId AND el.giftItem IS NOT NULL")
    List<EventLog> findWinnerLogsByEventId(@Param("eventId") Long eventId);
}
