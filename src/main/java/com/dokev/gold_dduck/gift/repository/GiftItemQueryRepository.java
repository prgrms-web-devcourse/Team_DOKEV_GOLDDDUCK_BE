package com.dokev.gold_dduck.gift.repository;

import static com.dokev.gold_dduck.event.domain.QEvent.event;
import static com.dokev.gold_dduck.event.domain.QEventLog.eventLog;
import static com.dokev.gold_dduck.gift.domain.QGift.gift;
import static com.dokev.gold_dduck.gift.domain.QGiftItem.giftItem;

import com.dokev.gold_dduck.gift.dto.GiftItemDetailDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class GiftItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public GiftItemQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<GiftItemDetailDto> searchDescByMember(
        Long memberId,
        GiftItemSearchCondition giftItemSearchCondition,
        Pageable pageable
    ) {
        List<GiftItemDetailDto> content = queryFactory
            .select(Projections.fields(GiftItemDetailDto.class,
                giftItem.id,
                giftItem.giftType,
                giftItem.content,
                giftItem.used,
                gift.category,
                event.mainTemplate
            ))
            .from(eventLog)
            .join(eventLog.giftItem, giftItem)
            .on(eventLog.member.id.eq(memberId))
            .join(giftItem.gift, gift)
            .join(eventLog.event, event)
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
