package com.dokev.gold_dduck.member.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RoleGroupType {

    USER("USER_GROUP", "일반 사용자 그룹"),
    ADMIN("ADMIN_GROUP", "관리자 그룹"),
    GUEST("GUEST_GROUP", "게스트 그룹");

    private final String code;

    private final String displayName;

    RoleGroupType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static RoleGroupType of(String code) {
        return Arrays.stream(RoleGroupType.values())
            .filter(role -> role.getCode().equals(code))
            .findAny()
            .orElse(GUEST);
    }
}
