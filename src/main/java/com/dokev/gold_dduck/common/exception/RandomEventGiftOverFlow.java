package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class RandomEventGiftOverFlow extends BusinessException {

    public RandomEventGiftOverFlow() {
        super(ErrorCode.GIFT_OVER_FLOW);
    }
}
