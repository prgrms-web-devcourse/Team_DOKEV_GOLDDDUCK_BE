package com.dokev.gold_dduck.config;

import com.dokev.gold_dduck.jwt.Jwt;
import com.dokev.gold_dduck.jwt.JwtAuthenticationFilter;
import com.dokev.gold_dduck.jwt.JwtProperties;
import com.dokev.gold_dduck.member.domain.RoleType;
import com.dokev.gold_dduck.member.service.MemberService;
import com.dokev.gold_dduck.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.dokev.gold_dduck.oauth2.OAuth2AuthenticationSuccessHandler;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsUtils;

@Slf4j
@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtProperties jwtProperties;

    private final MemberService memberService;

    public WebSecurityConfiguration(JwtProperties jwtProperties, MemberService memberService) {
        this.jwtProperties = jwtProperties;
        this.memberService = memberService;
    }

    @Bean
    public Jwt jwt() {
        return new Jwt(
            jwtProperties.getIssuer(),
            jwtProperties.getClientSecret(),
            jwtProperties.getExpirySeconds()
        );
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        Jwt jwt = getApplicationContext().getBean(Jwt.class);
        return new JwtAuthenticationFilter(jwtProperties.getHeader(), jwt);
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        Jwt jwt = getApplicationContext().getBean(Jwt.class);
        return new OAuth2AuthenticationSuccessHandler(jwt, memberService, authorizationRequestRepository());
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication != null ? authentication.getPrincipal() : null;
            log.warn("{} is denied", principal, e);
            response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                e.getMessage()
            );
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            log.warn("request is denied", e);
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                e.getMessage()
            );
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
            "login/**",
            "oauth2/**",
            "/h2-console/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .authorizeRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode(), RoleType.ADMIN.getCode())
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .headers().disable()
            .httpBasic().disable()
            .rememberMe().disable()
            .logout().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorization")
            .authorizationRequestRepository(authorizationRequestRepository())
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler())
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
            .accessDeniedHandler(accessDeniedHandler())
            .and()
            .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);

    }
}
