package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.Event;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    @Transactional
    @Query("select distinct e from Event e"
        + " join fetch e.gifts"
        + " join fetch e.member"
        + " where e.code = :eventCode AND e.deletedAt IS null")
    Optional<Event> findEventByCodeWithGift(@Param("eventCode") UUID eventCode);

}
