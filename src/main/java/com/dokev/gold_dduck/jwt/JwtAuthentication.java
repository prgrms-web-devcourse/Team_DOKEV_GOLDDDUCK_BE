package com.dokev.gold_dduck.jwt;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import lombok.ToString;
import org.springframework.util.Assert;

@ToString(of = {"userId", "username"})
public class JwtAuthentication {

    public final Long userId;

    public final String username;


    public JwtAuthentication(Long userId, String username) {
        Assert.notNull(userId, "userId must be provided.");
        Assert.isTrue(isNotEmpty(username), "username must be provided.");

        this.userId = userId;
        this.username = username;
    }
}
