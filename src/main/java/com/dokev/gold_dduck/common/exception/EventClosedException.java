package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class EventClosedException extends BusinessException{

    public EventClosedException() {
        super(ErrorCode.EVENT_CLOSED);
    }
}
