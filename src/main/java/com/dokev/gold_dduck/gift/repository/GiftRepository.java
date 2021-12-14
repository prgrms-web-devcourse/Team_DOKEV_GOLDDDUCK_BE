package com.dokev.gold_dduck.gift.repository;

import com.dokev.gold_dduck.gift.domain.Gift;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GiftRepository extends JpaRepository<Gift, Long> {

    @Query("select distinct g from Gift g"
        + " join fetch g.giftItems gl"
        + " where g.event.id = :eventId and gl.member is null")
    List<Gift> findByEventId(@Param("eventId") Long eventId);
}
