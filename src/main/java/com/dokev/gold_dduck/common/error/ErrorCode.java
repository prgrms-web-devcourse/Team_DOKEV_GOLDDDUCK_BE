package com.dokev.gold_dduck.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNEXPECTED("-1", "Unexpected exception occurred"),

    ENTITY_NOT_FOUND("E001", "해당 엔티티를 찾을 수 없습니다. name : %s, id : %s "),
    EVENT_ALREADY_PARTICIPATED("E002", "이미 참여한 이벤트 입니다."),
    EVENT_CLOSED("E003", "종료된 이벤트 입니다."),

    GIFT_ALREADY_ALLOCATED("G001", "GiftItem(id=%d)은 이미 Member(id=%d)에게 할당된 선물입니다."),
    GIFT_STOCK_OUT("G002", "Gift(id=%d)의 GiftItem 재고가 전부 소진되었습니다."),
    GIFT_BLANK_DRAWN("G004", "꽝을 뽑았습니다."),

    INVALID_INPUT_VALUE("C004", "Invalid Input Value"),
    METHOD_NOT_ALLOWED("C005", "Method not allowed"),
    GIFT_NOT_EMPTY("C006", "Gift or GiftItem is must not be empty"),

    FILE_UPLOAD_FAILURE("F001", "파일 업로드에 실패하였습니다."),

    MEMBER_GIFT_NOT_MATCHED("M001", "멤버 id : %d가 선물 id :%d을 가지고 있지 않습니다."),
    MEMBER_EVENT_NOT_MATCHED("M002", "멤버 id : %d가 이벤트 id : %d를 가지고 있지 않습니다.");

    private final String code;

    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
