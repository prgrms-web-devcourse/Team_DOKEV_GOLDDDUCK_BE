package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class GiftBlankDrawnException extends BusinessException{

    public GiftBlankDrawnException() {
        super(ErrorCode.GIFT_BLANK_DRAWN);
    }
}
