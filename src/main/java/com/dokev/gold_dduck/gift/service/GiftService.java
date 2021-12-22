package com.dokev.gold_dduck.gift.service;

import com.dokev.gold_dduck.common.exception.EntityNotFoundException;
import com.dokev.gold_dduck.common.exception.EventAlreadyParticipatedException;
import com.dokev.gold_dduck.common.exception.EventClosedException;
import com.dokev.gold_dduck.common.exception.GiftBlankDrawnException;
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
import com.dokev.gold_dduck.gift.dto.GiftItemSearchDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSimpleDto;
import com.dokev.gold_dduck.gift.dto.GiftItemUpdateDto;
import com.dokev.gold_dduck.gift.repository.GiftItemQueryRepository;
import com.dokev.gold_dduck.gift.repository.GiftItemRepository;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.domain.RoleGroupType;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    @Transactional(noRollbackFor = {EventClosedException.class, GiftStockOutException.class})
    public GiftItemDto chooseGiftItemByFIFO(Long eventId, Long memberId, Long giftId) {
        Event event = eventRepository.findByIdForUpdate(eventId)
            .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        event.validateEventRunning();
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
        checkAlreadyParticipatedMember(event, member);
        Gift gift = giftRepository.findById(giftId)
            .orElseThrow(() -> new EntityNotFoundException(Gift.class, giftId));

        Optional<GiftItem> chosenGiftItem = findGiftItemByFIFO(giftId);
        eventLogRepository.save(new EventLog(event, member, gift, chosenGiftItem.orElse(null)));
        if (chosenGiftItem.isPresent()) {
            if (event.getLeftGiftCount() <= 1) {
                event.closeEvent();
            }
            event.decreaseLeftGiftCount();
            chosenGiftItem.get().allocateMember(member);
            return giftConverter.convertToGiftItemDto(chosenGiftItem.get());
        }
        throw new GiftStockOutException(giftId);
    }

    @Transactional(noRollbackFor = {EventClosedException.class, GiftStockOutException.class})
    public GiftItemDto chooseGiftItemByFIFOV2(Long eventId, Long memberId, Long giftId) {
        Event event = eventRepository.findByIdForUpdate(eventId)
            .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        event.validateEventRunning();
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
        checkAlreadyParticipatedMember(event, member);
        Gift gift = giftRepository.findById(giftId)
            .orElseThrow(() -> new EntityNotFoundException(Gift.class, giftId));

        Optional<GiftItem> chosenGiftItem = findGiftItemByFIFO(giftId);
        if (chosenGiftItem.isPresent()) {
            if (event.getLeftGiftCount() <= 1) {
                event.closeEvent();
            }
            event.decreaseLeftGiftCount();
            chosenGiftItem.get().allocateMember(member);
            eventLogRepository.save(new EventLog(event, member, gift, chosenGiftItem.get()));
            return giftConverter.convertToGiftItemDto(chosenGiftItem.get());
        }
        throw new GiftStockOutException(giftId);
    }

    @Transactional(noRollbackFor = {EventClosedException.class, GiftBlankDrawnException.class})
    public GiftItemDetailDto chooseGiftItemByRandom(Long eventId, Long memberId) {
        Event event = eventRepository.findByIdForUpdate(eventId)
            .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        event.validateEventRunning();
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
        checkAlreadyParticipatedMember(event, member);

        Integer leftBlankCount = event.getLeftBlankCount();
        List<GiftItem> giftItems = giftRepository.findByEventId(eventId).stream()
            .flatMap(gift -> gift.getGiftItems().stream())
            .collect(Collectors.toList());

        if (giftItems.size() + leftBlankCount <= 1) {
            event.closeEvent();
        }

        int nextInt = new Random().nextInt(leftBlankCount + giftItems.size());
        if (giftItems.size() > nextInt) {
            GiftItem chosenGiftItem = giftItems.get(nextInt);
            event.decreaseLeftGiftCount();
            chosenGiftItem.allocateMember(member);
            eventLogRepository.save(new EventLog(event, member, chosenGiftItem.getGift(), chosenGiftItem));
            return giftConverter.convertToGiftItemDetailDto(chosenGiftItem, chosenGiftItem.getGift().getId(),
                chosenGiftItem.getGift().getCategory(),
                event.getMainTemplate(), null);
        } else {
            event.decreaseLeftBlankCount();
            eventLogRepository.save(new EventLog(event, member, null, null));
            throw new GiftBlankDrawnException();
        }
    }

    @Transactional(noRollbackFor = {EventClosedException.class, GiftBlankDrawnException.class})
    public GiftItemDetailDto chooseGiftItemByRandom2(Long eventId, Long memberId) {
        Event event = eventRepository.findByIdForUpdate(eventId)
            .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        event.validateEventRunning();
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
        checkAlreadyParticipatedMember(event, member);

        int candidateCount = event.getLeftGiftCount() + event.getLeftBlankCount();
        if (candidateCount <= 1) {
            event.closeEvent();
        }
        int offset = new Random().nextInt(candidateCount);
        Optional<GiftItemDetailDto> maybeGiftItem = giftItemQueryRepository.findDetailGiftItemByRandom(eventId,
            offset);
        if (maybeGiftItem.isPresent()) {
            GiftItemDetailDto chosenGiftItem = maybeGiftItem.get();
            event.decreaseLeftGiftCount();
            giftItemRepository.allocateMemberToGiftItem(chosenGiftItem.getId(), memberId);
            eventLogRepository.save(
                new EventLog(event, member, giftRepository.getById(chosenGiftItem.getGiftId()),
                    giftItemRepository.getById(chosenGiftItem.getId())));
            return chosenGiftItem;
        } else {
            event.decreaseLeftBlankCount();
            eventLogRepository.save(new EventLog(event, member, null, null));
            throw new GiftBlankDrawnException();
        }
    }

    @Transactional(noRollbackFor = {EventClosedException.class, GiftBlankDrawnException.class})
    public GiftItemSimpleDto chooseGiftItemByRandom3(Long eventId, Long memberId) {
        Event event = eventRepository.findByIdForUpdate(eventId)
            .orElseThrow(() -> new EntityNotFoundException(Event.class, eventId));
        event.validateEventRunning();
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
        checkAlreadyParticipatedMember(event, member);

        int candidateCount = event.getLeftGiftCount() + event.getLeftBlankCount();
        if (candidateCount <= 1) {
            event.closeEvent();
        }
        int offset = new Random().nextInt(candidateCount);
        Optional<GiftItemSimpleDto> maybeGiftItem = giftItemQueryRepository.findSimpleGiftItemByRandom(eventId,
            offset);
        if (maybeGiftItem.isPresent()) {
            GiftItemSimpleDto chosenGiftItem = maybeGiftItem.get();
            event.decreaseLeftGiftCount();
            giftItemRepository.allocateMemberToGiftItem(chosenGiftItem.getGiftItemId(), memberId);
            eventLogRepository.save(
                new EventLog(event, member, giftRepository.getById(chosenGiftItem.getGiftId()),
                    giftItemRepository.getById(chosenGiftItem.getGiftItemId())));
            return chosenGiftItem;
        } else {
            event.decreaseLeftBlankCount();
            eventLogRepository.save(new EventLog(event, member, null, null));
            throw new GiftBlankDrawnException();
        }
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
            .orElseThrow(() -> new MemberGiftNotMatchedException(memberId, giftItemId));
        giftItem.changeUsed(giftItemUpdateDto.getUsed());
    }

    private void checkAlreadyParticipatedMember(Event event, Member member) {
        if (RoleGroupType.of(member.getGroup().getName()) == RoleGroupType.ADMIN) {
            return;
        }
        boolean alreadyParticipated = eventLogRepository.existsByEventIdAndMemberId(event.getId(), member.getId());
        if (alreadyParticipated) {
            throw new EventAlreadyParticipatedException();
        }
    }

    private Optional<GiftItem> findGiftItemByFIFO(Long giftId) {
        List<GiftItem> chosenGiftItems = giftItemRepository.findByGiftIdWithPage(giftId, Pageable.ofSize(1));
        return chosenGiftItems.isEmpty() ? Optional.empty() : Optional.of(chosenGiftItems.get(0));
    }

    public GiftItemSearchDto searchGiftItem(Long giftItemId) {
        return giftItemQueryRepository.findGiftItemDetailById(giftItemId)
            .orElseThrow(() -> new EntityNotFoundException(GiftItem.class, giftItemId));
    }
}
