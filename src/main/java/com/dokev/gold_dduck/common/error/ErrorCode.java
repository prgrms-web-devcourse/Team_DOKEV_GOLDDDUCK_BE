package com.dokev.gold_dduck.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNEXPECTED("-1", "Unexpected exception occurred"),
    INVALID_INPUT_VALUE("C004", "Invalid Input Value"),
    METHOD_NOT_ALLOWED("C005", "Method not allowed"),
    MEMBER_NOT_FOUND("C006", "Member is not found"),
    GIFT_NOT_EMPTY("C007", "Gift or GiftItem is must be not empty");

    private final String code;

    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
