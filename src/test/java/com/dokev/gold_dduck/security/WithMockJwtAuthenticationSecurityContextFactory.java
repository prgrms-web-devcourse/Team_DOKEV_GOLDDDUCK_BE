package com.dokev.gold_dduck.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.dokev.gold_dduck.jwt.JwtAuthentication;
import com.dokev.gold_dduck.jwt.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtAuthenticationSecurityContextFactory implements
    WithSecurityContextFactory<WithMockJwtAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
            new JwtAuthentication(
                annotation.id(),
                annotation.name()),
            null,
            createAuthorityList(annotation.role().getCode())
        );
        context.setAuthentication(authentication);
        return context;
    }
}
