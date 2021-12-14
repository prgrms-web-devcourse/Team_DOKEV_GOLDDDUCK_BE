package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class GiftAlreadyAllocatedException extends BusinessException {

    public GiftAlreadyAllocatedException(Long giftItemId, Long memberId) {
        super(ErrorCode.GIFT_ALREADY_ALLOCATED, giftItemId, memberId);
    }
}
