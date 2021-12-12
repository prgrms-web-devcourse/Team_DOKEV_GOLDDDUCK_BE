package com.dokev.gold_dduck.gift.service;

import com.dokev.gold_dduck.common.exception.EntityNotFoundException;
import com.dokev.gold_dduck.common.exception.EventAlreadyParticipatedException;
import com.dokev.gold_dduck.common.exception.GiftStockOutException;
import com.dokev.gold_dduck.common.exception.MemberGiftNotMatchedException;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.repository.EventLogRepository;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.gift.converter.GiftConverter;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftItemDetailDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.gift.dto.GiftItemListDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchCondition;
import com.dokev.gold_dduck.gift.dto.GiftItemUpdateDto;
import com.dokev.gold_dduck.gift.repository.GiftItemQueryRepository;
import com.dokev.gold_dduck.gift.repository.GiftItemRepository;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class GiftService {

    private final EventRepository eventRepository;

    private final GiftRepository giftRepository;

    private final GiftItemRepository giftItemRepository;

    private final MemberRepository memberRepository;

    private final EventLogRepository eventLogRepository;

    private final GiftItemQueryRepository giftItemQueryRepository;

    private final GiftConverter giftConverter;

    public GiftService(
        EventRepository eventRepository,
        GiftRepository giftRepository,
        GiftItemRepository giftItemRepository,
        MemberRepository memberRepository,
        EventLogRepository eventLogRepository,
        GiftItemQueryRepository giftItemQueryRepository,
        GiftConverter giftConverter
    ) {
        this.eventRepository = eventRepository;
        this.giftRepository = giftRepository;
        this.giftItemRepository = giftItemRepository;
        this.memberRepository = memberRepository;
        this.eventLogRepository = eventLogRepository;
        this.giftItemQueryRepository = giftItemQueryRepository;
        this.giftConverter = giftConverter;
    }

    @Transactional
    public GiftItemDto chooseGiftItemByFIFO(Long eventId, Long memberId, Long giftId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
        boolean alreadyParticipated = eventLogRepository.existsByEventIdAndMemberId(eventId, memberId);
        if (alreadyParticipated) {
            throw new EventAlreadyParticipatedException();
        }
        Gift gift = giftRepository.findById(giftId)
            .orElseThrow(() -> new EntityNotFoundException(Gift.class, giftId));

        Optional<GiftItem> chosenGiftItem = findGiftItemByFIFO(giftId);
        eventLogRepository.save(new EventLog(event, member, gift, chosenGiftItem.orElse(null)));
        if (chosenGiftItem.isPresent()) {
            chosenGiftItem.get().allocateMember(member);
            return giftConverter.convertToGiftItemDto(chosenGiftItem.get());
        }
        throw new GiftStockOutException();
    }

    public GiftItemListDto searchDescByMember(
        Long memberId,
        GiftItemSearchCondition giftItemSearchCondition,
        Pageable pageable
    ) {
        if (!memberRepository.existsById(memberId)) {
            throw new EntityNotFoundException(Member.class, memberId);
        }
        Page<GiftItemDetailDto> page = giftItemQueryRepository.searchDescByMember(memberId, giftItemSearchCondition,
            pageable);
        return new GiftItemListDto(page);
    }

    @Transactional
    public void updateGiftItem(Long memberId, Long giftItemId, GiftItemUpdateDto giftItemUpdateDto) {
        if (!memberRepository.existsById(memberId)) {
            throw new EntityNotFoundException(Member.class, memberId);
        }
        GiftItem giftItem = giftItemRepository.findById(giftItemId)
            .orElseThrow(() -> new EntityNotFoundException(GiftItem.class, giftItemId));
        Optional.ofNullable(giftItem.getMember())
            .filter(member -> member.getId().equals(memberId))
            .orElseThrow(() -> {throw new MemberGiftNotMatchedException(memberId, giftItemId);});
        giftItem.changeUsed(giftItemUpdateDto.getUsed());
    }

    private Optional<GiftItem> findGiftItemByFIFO(Long giftId) {
        List<GiftItem> chosenGiftItems = giftItemRepository.findByGiftIdWithPageForUpdate(giftId, Pageable.ofSize(1));
        return chosenGiftItems.isEmpty() ? Optional.empty() : Optional.of(chosenGiftItems.get(0));
    }
}
