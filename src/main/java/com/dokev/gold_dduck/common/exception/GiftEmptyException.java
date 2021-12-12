package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class GiftEmptyException extends BusinessException {

    public GiftEmptyException(String message) {
        super(ErrorCode.GIFT_NOT_EMPTY);
    }
}
