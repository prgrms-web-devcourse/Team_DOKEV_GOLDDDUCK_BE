package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
