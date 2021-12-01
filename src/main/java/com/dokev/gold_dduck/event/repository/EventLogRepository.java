package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}
