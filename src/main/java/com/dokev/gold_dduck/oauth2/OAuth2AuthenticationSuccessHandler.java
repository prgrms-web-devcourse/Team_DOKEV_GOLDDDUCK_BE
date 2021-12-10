package com.dokev.gold_dduck.oauth2;

import static com.dokev.gold_dduck.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.dokev.gold_dduck.common.util.CookieUtil;
import com.dokev.gold_dduck.jwt.Jwt;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.domain.RoleType;
import com.dokev.gold_dduck.member.service.MemberService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Jwt jwt;

    private final MemberService memberService;

    private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    public OAuth2AuthenticationSuccessHandler(Jwt jwt, MemberService memberService,
        HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository) {
        this.jwt = jwt;
        this.memberService = memberService;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws ServletException, IOException {
        Object principal = authentication.getPrincipal();
        log.info("OAuth2 authentication success: {}", principal);

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            String provider = oauth2Token.getAuthorizedClientRegistrationId();
            Member member = memberService.join(oauth2User, provider);
            String token = generateToken(member);
            String redirectUri = determineTargetUrl(request, response, authentication);
            String targetUrl = UriComponentsBuilder
                .fromUriString(redirectUri).queryParam("token", token)
                .build().toUriString();
            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private String generateToken(Member member) {
        return jwt.sign(Jwt.Claims.of(member.getId(), member.getName(), new String[]{RoleType.USER.getCode()}));
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue);
        return redirectUri.orElse(getDefaultTargetUrl());
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
