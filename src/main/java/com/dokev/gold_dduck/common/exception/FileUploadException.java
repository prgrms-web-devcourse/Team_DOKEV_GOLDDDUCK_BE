package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class FileUploadException extends BusinessException {

    public FileUploadException(String message) {
        super(message, ErrorCode.GIFT_NOT_EMPTY);
    }
}
