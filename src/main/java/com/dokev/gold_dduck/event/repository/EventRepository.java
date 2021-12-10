package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.Event;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT distinct  e FROM Event e"
            + " JOIN FETCH e.gifts"
            + " where e.code = :eventCode")
    Optional<Event> findEventByCodeWithGift(@Param("eventCode") UUID eventCode);

}
