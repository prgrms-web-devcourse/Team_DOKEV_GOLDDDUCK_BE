package com.dokev.gold_dduck.gift.repository;

import static com.dokev.gold_dduck.event.domain.QEventLog.eventLog;
import static com.dokev.gold_dduck.gift.domain.QGift.gift;
import static com.dokev.gold_dduck.gift.domain.QGiftItem.giftItem;

import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class GiftItemRepositoryCustomImpl implements GiftItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GiftItemRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<GiftItem> searchDescByMember(
        Long memberId,
        GiftItemSearchCondition giftItemSearchCondition,
        Pageable pageable
    ) {
        List<GiftItem> content = queryFactory
            .select(eventLog.giftItem)
            .from(eventLog)
            .join(eventLog.giftItem, giftItem)
            .on(eventLog.member.id.eq(memberId))
            .join(giftItem.gift, gift).fetchJoin()
            .where(usedEq(giftItemSearchCondition.getUsed()))
            .orderBy(eventLog.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .select(eventLog.giftItem)
            .from(eventLog)
            .join(eventLog.giftItem, giftItem)
            .on(eventLog.member.id.eq(memberId))
            .where(usedEq(giftItemSearchCondition.getUsed()))
            .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression usedEq(Boolean used) {
        return used != null ? giftItem.used.eq(used) : null;
    }
}
