package com.dokev.gold_dduck.gift.repository;

import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftItemRepositoryCustom {

    Page<GiftItem> searchDescByMember(Long memberId, GiftItemSearchCondition giftItemSearchCondition,
        Pageable pageable);
}
