package com.dokev.gold_dduck.gift.repository;

import com.dokev.gold_dduck.gift.domain.GiftItem;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GiftItemRepository extends JpaRepository<GiftItem, Long> {

    @Query("select gl from GiftItem gl"
        + " where gl.gift.id = :giftId and gl.member is null")
    List<GiftItem> findByGiftIdWithPage(@Param("giftId") Long giftId, Pageable pageable);

    @Modifying
    @Query("update GiftItem gi"
        + " set gi.member.id = :memberId"
        + " where gi.id = :giftId")
    int allocateMemberToGiftItem(@Param("giftId") Long giftId, @Param("memberId") Long memberId);
}
