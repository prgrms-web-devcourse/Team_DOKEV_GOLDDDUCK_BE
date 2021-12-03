package com.dokev.gold_dduck.gift.repository;

import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GiftRepository extends JpaRepository<Gift, Long> {

    @Query(value = "SELECT distinct gi From GiftItem gi JOIN FETCH gi.gift")
    List<GiftItem> findGiftItemsByGiftId(@Param("id") Long giftId);
}
