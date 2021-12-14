package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.Event;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    @Query("select distinct e from Event e"
        + " join fetch e.gifts"
        + " join fetch e.member"
        + " where e.code = :eventCode AND e.deletedAt IS null")
    Optional<Event> findEventByCodeWithGift(@Param("eventCode") UUID eventCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e"
        + " where e.id = :eventId")
    Optional<Event> findByIdForUpdate(@Param("eventId") Long eventId);
}
