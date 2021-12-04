package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException(Long memberId) {
        super(String.valueOf(memberId), ErrorCode.MEMBER_NOT_FOUND);
    }
}
