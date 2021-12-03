package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.gift.domain.Gift;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findEventByCode(UUID code);

    @Query(value = "SELECT distinct g From Gift g JOIN FETCH g.event")
    List<Gift> findGiftsByEventId(@Param("id") Long eventId);
}
