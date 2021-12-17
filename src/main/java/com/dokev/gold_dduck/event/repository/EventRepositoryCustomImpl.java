package com.dokev.gold_dduck.event.repository;

import static com.dokev.gold_dduck.event.domain.QEvent.event;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.dto.EventSearchCondition;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EventRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Event> searchSimpleDescByMember(Long memberId, EventSearchCondition eventSearchCondition,
        Pageable pageable) {
        QueryResults<Event> eventQueryResults = queryFactory
            .selectFrom(event)
            .where(memberEq(memberId), eventProgressStatusEq(eventSearchCondition.getEventProgressStatus()),
                event.deletedAt.isNull())
            .orderBy(event.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();
        return new PageImpl<>(eventQueryResults.getResults(), pageable, eventQueryResults.getTotal());
    }

    private BooleanExpression eventProgressStatusEq(EventProgressStatus eventProgressStatus) {
        return eventProgressStatus != null ? event.eventProgressStatus.eq(eventProgressStatus) : null;
    }

    private BooleanExpression memberEq(Long memberId) {
        return memberId != null ? event.member.id.eq(memberId) : null;
    }
}
