package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class GiftStockOutException extends BusinessException {

    public GiftStockOutException(Long giftId) {
        super(ErrorCode.GIFT_STOCK_OUT, giftId);
    }
}
