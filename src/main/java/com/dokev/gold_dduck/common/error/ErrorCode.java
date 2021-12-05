package com.dokev.gold_dduck.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNEXPECTED("-1", "Unexpected exception occurred"),
    INVALID_INPUT_VALUE("C004", "Invalid Input Value"),
    METHOD_NOT_ALLOWED("C005", "Method not allowed");

    private final String code;

    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
