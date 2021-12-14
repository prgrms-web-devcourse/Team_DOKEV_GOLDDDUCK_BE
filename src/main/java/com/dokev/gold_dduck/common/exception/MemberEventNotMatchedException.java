package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class MemberEventNotMatchedException extends BusinessException {

    public MemberEventNotMatchedException(Long memberId, Long eventId) {
        super(ErrorCode.MEMBER_EVENT_NOT_MATCHED, memberId, eventId);
    }
}
