package com.codepractice.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import com.codepractice.auth_service.service.impl.CustomOAuth2UserService;
import com.codepractice.auth_service.service.impl.CustomUserDetailService;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService CustomUserDetailService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
                .authorizationServer();
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer,
                        (authorizationSever) -> authorizationSever.oidc(Customizer.withDefaults()))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/oauth2/**", "/css/**", "/js/**", "/error").permitAll().anyRequest()
                        .authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login").defaultSuccessUrl("/", true)
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)))
                .formLogin(form -> form
                        .loginPage("/login").defaultSuccessUrl("/", true))
                .userDetailsService(CustomUserDetailService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
