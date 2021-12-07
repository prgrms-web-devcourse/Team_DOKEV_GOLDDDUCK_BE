package com.dokev.gold_dduck.gift.controller;

import com.dokev.gold_dduck.common.ApiResponse;
import com.dokev.gold_dduck.gift.dto.GiftFifoChoiceDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.gift.service.GiftService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@RestController
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @PostMapping("v1/gifts/fifo")
    public ApiResponse<GiftItemDto> chooseGiftItemByFIFO(@RequestBody @Validated GiftFifoChoiceDto giftFifoChoiceDto) {
        GiftItemDto giftItemDto = giftService.chooseGiftItemByFIFO(giftFifoChoiceDto.getEventId(),
            giftFifoChoiceDto.getMemberId(), giftFifoChoiceDto.getGiftId());
        return ApiResponse.success(giftItemDto);
    }
}
