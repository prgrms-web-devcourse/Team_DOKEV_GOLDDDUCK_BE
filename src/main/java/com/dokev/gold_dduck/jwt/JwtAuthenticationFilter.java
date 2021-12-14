package com.dokev.gold_dduck.jwt;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.CollectionUtils.isEmpty;

import com.dokev.gold_dduck.jwt.Jwt.Claims;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

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
                    Claims claims = jwt.verify(token);
                    log.debug("Jwt parse result: {}", claims);

                    Long userId = claims.userId;
                    String username = claims.username;
                    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(claims.roles);

                    if (nonNull(userId) && isNotEmpty(username) && !isEmpty(authorities)) {
                        JwtAuthenticationToken authentication
                            = new JwtAuthenticationToken(new JwtAuthentication(userId, username), null, authorities);
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
            String decodeToken = URLDecoder.decode(token, StandardCharsets.UTF_8);
            String[] parts = decodeToken.split(" ");
            if (parts.length == 2) {
                String scheme = parts[0];
                String credentials = parts[1];
                return BEARER.matcher(scheme).matches() ? credentials : null;
            }
        }
        return null;
    }
}
