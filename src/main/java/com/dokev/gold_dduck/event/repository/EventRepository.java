package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.gift.domain.Gift;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event findEventByCode(@Param("code") UUID code);

    @Query(value = "SELECT a FROM Gift AS a WHERE a.event = :eventId")
    List<Gift> findGiftsByEventId(Long eventId);
}
