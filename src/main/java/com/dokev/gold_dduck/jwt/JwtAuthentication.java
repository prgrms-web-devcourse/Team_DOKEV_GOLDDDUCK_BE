package com.dokev.gold_dduck.jwt;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

public class JwtAuthentication {

    public final String token;

    public final String username;


    public JwtAuthentication(String token, String username) {
        Assert.isTrue(isNotEmpty(token), "token must be provided.");
        Assert.isTrue(isNotEmpty(username), "username must be provided.");

        this.token = token;
        this.username = username;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("token", token)
            .append("username", username)
            .toString();
    }
}
