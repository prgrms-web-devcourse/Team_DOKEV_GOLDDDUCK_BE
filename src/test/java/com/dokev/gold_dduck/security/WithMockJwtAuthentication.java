package com.dokev.gold_dduck.security;

import com.dokev.gold_dduck.member.domain.RoleType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {

    long id() default 1L;

    String name() default "dokev_user";

    RoleType role() default RoleType.USER;

}
