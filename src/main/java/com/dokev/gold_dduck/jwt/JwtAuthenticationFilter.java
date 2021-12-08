package com.dokev.gold_dduck.jwt;

import com.dokev.gold_dduck.jwt.Jwt.Claims;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final String headerKey;

    private final Jwt jwt;

    public JwtAuthenticationFilter(String headerKey, Jwt jwt) {
        this.headerKey = headerKey;
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getToken(req);
            if (token != null) {
                try {
                    Claims claims = verify(token);
                    log.debug("Jwt parse result: {}", claims);

                    String username = claims.username;
                    List<GrantedAuthority> authorities = getAuthorities(claims);

                    if (username != null && !username.isEmpty() && authorities.size() > 0) {
                        UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.warn("Jwt processing filed: {}", e.getMessage());
                }
            }
        } else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained: {}",
                SecurityContextHolder.getContext().getAuthentication());
        }

        chain.doFilter(req, res);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(headerKey);
        if (token != null && !token.isEmpty()) {
            log.debug("Jwt token detected : {}", token);
            try {
                return URLDecoder.decode(token, "UTF-8");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0
            ? Collections.emptyList()
            : Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
