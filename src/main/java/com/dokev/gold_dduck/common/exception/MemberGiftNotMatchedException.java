package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class MemberGiftNotMatchedException extends BusinessException {

    public MemberGiftNotMatchedException(Long memberId, Long giftItemId) {
        super(ErrorCode.MEMBER_GIFT_NOT_MATCHED, memberId, giftItemId);
    }
}
