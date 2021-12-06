package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class EventAlreadyParticipatedException extends BusinessException {

    public EventAlreadyParticipatedException() {
        super(ErrorCode.EVENT_ALREADY_PARTICIPATED);
    }
}
