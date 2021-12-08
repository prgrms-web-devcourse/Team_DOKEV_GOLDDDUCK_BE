package com.dokev.gold_dduck.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString(of = {"header", "issuer", "clientSecret", "expirySeconds"})
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String header;

    private String issuer;

    private String clientSecret;

    private int expirySeconds;
}
