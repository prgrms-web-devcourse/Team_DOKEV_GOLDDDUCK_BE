package com.dokev.gold_dduck.gift.controller;

import com.dokev.gold_dduck.common.ApiResponse;
import com.dokev.gold_dduck.gift.dto.GiftFifoChoiceDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDetailDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.gift.dto.GiftItemListDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchCondition;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSimpleDto;
import com.dokev.gold_dduck.gift.dto.GiftItemUpdateDto;
import com.dokev.gold_dduck.gift.dto.GiftRandomChoiceDto;
import com.dokev.gold_dduck.gift.service.GiftService;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@RestController
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @PostMapping("/v1/gifts/fifo")
    public ApiResponse<GiftItemDto> chooseGiftItemByFIFO(@RequestBody @Validated GiftFifoChoiceDto giftFifoChoiceDto) {
        GiftItemDto giftItemDto = giftService.chooseGiftItemByFIFO(giftFifoChoiceDto.getEventId(),
            giftFifoChoiceDto.getMemberId(), giftFifoChoiceDto.getGiftId());
        return ApiResponse.success(giftItemDto);
    }

    @PostMapping("/v2/gifts/fifo")
    public ApiResponse<GiftItemDto> chooseGiftItemByFIFOV2(@RequestBody @Validated GiftFifoChoiceDto giftFifoChoiceDto) {
        GiftItemDto giftItemDto = giftService.chooseGiftItemByFIFOV2(giftFifoChoiceDto.getEventId(),
            giftFifoChoiceDto.getMemberId(), giftFifoChoiceDto.getGiftId());
        return ApiResponse.success(giftItemDto);
    }

    @PostMapping("/v1/gifts/random")
    public ApiResponse<GiftItemDetailDto> chooseGiftItemByRandom(
        @RequestBody @Valid GiftRandomChoiceDto giftRandomChoiceDto
    ) {
        GiftItemDetailDto giftItemDto = giftService.chooseGiftItemByRandom(
            giftRandomChoiceDto.getEventId(),
            giftRandomChoiceDto.getMemberId());
        return ApiResponse.success(giftItemDto);
    }

    @PostMapping("/v2/gifts/random")
    public ApiResponse<GiftItemDetailDto> chooseGiftItemByRandomV2(
        @RequestBody @Valid GiftRandomChoiceDto giftRandomChoiceDto
    ) {
        GiftItemDetailDto giftItemDto = giftService.chooseGiftItemByRandom2(
            giftRandomChoiceDto.getEventId(),
            giftRandomChoiceDto.getMemberId());
        return ApiResponse.success(giftItemDto);
    }

    @PostMapping("/v3/gifts/random")
    public ApiResponse<GiftItemSimpleDto> chooseGiftItemByRandomV3(
        @RequestBody @Valid GiftRandomChoiceDto giftRandomChoiceDto
    ) {
        GiftItemSimpleDto giftItemDto = giftService.chooseGiftItemByRandom3(
            giftRandomChoiceDto.getEventId(),
            giftRandomChoiceDto.getMemberId());
        return ApiResponse.success(giftItemDto);
    }

    @GetMapping("/v1/members/{memberId}/gifts")
    public ApiResponse<GiftItemListDto> searchDescByMember(
        @PathVariable Long memberId,
        @RequestParam(required = false) Boolean used,
        @PageableDefault(size = 4) Pageable pageable
    ) {
        GiftItemSearchCondition giftItemSearchCondition = new GiftItemSearchCondition(used);
        return ApiResponse.success(giftService.searchDescByMember(memberId, giftItemSearchCondition, pageable));
    }

    @PatchMapping("/v1/members/{memberId}/giftItems/{giftItemId}")
    public ApiResponse<Object> updateGiftItem(
        @PathVariable Long memberId,
        @PathVariable Long giftItemId,
        @RequestBody @Valid GiftItemUpdateDto giftItemUpdateDto
    ) {
        giftService.updateGiftItem(memberId, giftItemId, giftItemUpdateDto);
        return ApiResponse.success();
    }

    @Deprecated
    @GetMapping("/v1/members/{memberId}/giftItems/{giftItemId}")
    public ApiResponse<GiftItemSearchDto> searchGiftItemById
        (@PathVariable Long memberId, @PathVariable Long giftItemId) {
        GiftItemSearchDto giftItemSearchDto = giftService.searchGiftItem(giftItemId);
        return ApiResponse.success(giftItemSearchDto);
    }

    @GetMapping("/v2/giftItems/{giftItemId}")
    public ApiResponse<GiftItemSearchDto> searchGiftItemByIdV2(@PathVariable Long giftItemId) {
        GiftItemSearchDto giftItemSearchDto = giftService.searchGiftItem(giftItemId);
        return ApiResponse.success(giftItemSearchDto);
    }
}
