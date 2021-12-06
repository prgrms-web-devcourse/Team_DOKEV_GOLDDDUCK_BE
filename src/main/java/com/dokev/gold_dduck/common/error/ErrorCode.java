package com.dokev.gold_dduck.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    GIFT_CHOICE_TYPE_NOT_MATCHED("G002", "선물 선택 방식이 . expected : %s but actual : %s"),
    GIFT_ALREADY_ALLOCATED("G001", "이미 할당된 선물입니다."),
    EVENT_ALREADY_PARTICIPATED("E001", "이미 참여한 이벤트 입니다."),
    GIFT_STOCK_OUT("G001", "선물의 재고가 전부 소진되었습니다."),
    ENTITY_NOT_FOUND("E001", "해당 엔티티를 찾을 수 없습니다. name : %s, id : %s "),
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
