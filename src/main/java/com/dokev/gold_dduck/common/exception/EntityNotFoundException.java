package com.dokev.gold_dduck.common.exception;

import com.dokev.gold_dduck.common.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(Class<?> targetType, Long targetId) {
        super(ErrorCode.ENTITY_NOT_FOUND, targetType.getName(), String.valueOf(targetId));
    }

    public EntityNotFoundException(Class<?> targetType, String targetId) {
        super(ErrorCode.ENTITY_NOT_FOUND, targetType.getName(), targetId);
    }
}
