package com.dokev.gold_dduck.config;

import com.dokev.gold_dduck.jwt.Jwt;
import com.dokev.gold_dduck.jwt.JwtAuthenticationFilter;
import com.dokev.gold_dduck.jwt.JwtProperties;
import com.dokev.gold_dduck.member.service.MemberService;
import com.dokev.gold_dduck.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.dokev.gold_dduck.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

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
        return new OAuth2AuthenticationSuccessHandler(jwt, memberService);
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**", "login/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/user/me").hasAnyRole("USER", "ADMIN")
            .anyRequest().permitAll()
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
            .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);

    }
}
