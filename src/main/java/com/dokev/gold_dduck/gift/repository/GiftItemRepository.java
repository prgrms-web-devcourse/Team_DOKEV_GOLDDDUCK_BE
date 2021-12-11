package com.dokev.gold_dduck.gift.repository;

import com.dokev.gold_dduck.gift.domain.GiftItem;
import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GiftItemRepository extends JpaRepository<GiftItem, Long>, GiftItemRepositoryCustom{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select gl from GiftItem gl"
        + " where gl.gift.id = :giftId and gl.member is null")
    List<GiftItem> findByGiftIdWithPageForUpdate(@Param("giftId") Long giftId, Pageable pageable);
}
