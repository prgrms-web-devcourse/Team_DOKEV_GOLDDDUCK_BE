package com.dokev.gold_dduck.gift.repository;

import static com.dokev.gold_dduck.event.domain.QEvent.event;
import static com.dokev.gold_dduck.event.domain.QEventLog.eventLog;
import static com.dokev.gold_dduck.gift.domain.QGift.gift;
import static com.dokev.gold_dduck.gift.domain.QGiftItem.giftItem;
import static com.dokev.gold_dduck.member.domain.QMember.member;

import com.dokev.gold_dduck.gift.dto.GiftItemDetailDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchCondition;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSimpleDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
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
                event.mainTemplate,
                member.name.as("sender")
            ))
            .from(eventLog)
            .join(eventLog.giftItem, giftItem)
            .on(eventLog.member.id.eq(memberId))
            .join(giftItem.gift, gift)
            .join(eventLog.event, event)
            .join(event.member, member)
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

    public Optional<GiftItemSearchDto> findGiftItemDetailById(Long giftItemId) {
        GiftItemSearchDto giftItemSearchDto = queryFactory
            .select(Projections.fields(GiftItemSearchDto.class,
                giftItem.id,
                giftItem.giftType,
                giftItem.content,
                giftItem.used,
                gift.category,
                event.mainTemplate,
                member.name.as("sender"),
                eventLog.createdAt.as("receivedDate")
            ))
            .from(giftItem)
            .join(giftItem.gift, gift)
            .join(gift.event, event)
            .join(event.member, member)
            .leftJoin(eventLog).on(giftItem.id.eq(eventLog.giftItem.id))
            .where(giftItem.id.eq(giftItemId))
            .fetchOne();
        return Optional.ofNullable(giftItemSearchDto);
    }

    public Optional<GiftItemDetailDto> findDetailGiftItemByRandom(
        Long eventId,
        int offset
    ) {
        List<GiftItemDetailDto> content = queryFactory
            .select(Projections.fields(GiftItemDetailDto.class,
                giftItem.id,
                giftItem.giftType,
                giftItem.content,
                giftItem.used,
                gift.id.as("giftId"),
                gift.category,
                event.mainTemplate,
                event.member.name.as("sender")
            ))
            .from(gift)
            .join(gift.giftItems, giftItem)
            .on(gift.event.id.eq(eventId))
            .join(gift.event, event)
            .join(event.member, member)
            .where(giftItem.member.isNull())
            .offset(offset)
            .limit(1)
            .fetch();

        return content.isEmpty() ? Optional.empty() : Optional.of(content.get(0));
    }

    public Optional<GiftItemSimpleDto> findSimpleGiftItemByRandom(
        Long eventId,
        int offset
    ) {
        List<GiftItemSimpleDto> content = queryFactory
            .select(Projections.fields(GiftItemSimpleDto.class,
                giftItem.id.as("giftItemId"),
                gift.id.as("giftId")
            ))
            .from(gift)
            .join(gift.giftItems, giftItem)
            .on(gift.event.id.eq(eventId))
            .where(giftItem.member.isNull())
            .offset(offset)
            .limit(1)
            .fetch();

        return content.isEmpty() ? Optional.empty() : Optional.of(content.get(0));
    }

    private BooleanExpression usedEq(Boolean used) {
        return used != null ? giftItem.used.eq(used) : null;
    }
}
