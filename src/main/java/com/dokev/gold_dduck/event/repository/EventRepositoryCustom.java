package com.dokev.gold_dduck.event.repository;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {

    Page<Event> searchSimpleDescByMember(Long memberId, EventSearchCondition eventSearchCondition, Pageable pageable);
}
