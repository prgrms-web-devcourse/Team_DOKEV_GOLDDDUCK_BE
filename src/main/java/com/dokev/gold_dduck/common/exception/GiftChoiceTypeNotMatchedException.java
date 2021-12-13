package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class GiftChoiceTypeNotMatchedException extends BusinessException {

    public GiftChoiceTypeNotMatchedException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
