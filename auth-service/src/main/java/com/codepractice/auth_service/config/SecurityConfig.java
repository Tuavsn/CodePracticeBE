package com.codepractice.auth_service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.codepractice.auth_service.service.impl.CustomOAuth2UserService;
import com.codepractice.auth_service.service.impl.CustomUserDetailService;

@Configuration
public class SecurityConfig {
	@Value("${auth.server.gatewayClientUrl}")
    private String gatewayClientUrl;
	
	@Value("${auth.server.clientUrl}")
    private String clientUrl;
	
	private final CustomUserDetailService customUserDetailService;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final static String[] whiteList = {
		"/login/**",
		"/logout/**",
		"/register/**",
		"/forgot-password/**",
		"/confirm-registration/**",
		"/reset-password/**",
		"/oauth2/**",
		"/css/**",
		"/js/**",
		"/error/**",
	};

	public SecurityConfig(CustomUserDetailService customUserDetailService, 
                         CustomOAuth2UserService customOAuth2UserService) {
        this.customUserDetailService = customUserDetailService;
        this.customOAuth2UserService = customOAuth2UserService;
    }


	@Bean
	@Order(1)
	public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
			.authorizationServer();
		http
			.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
			.with(authorizationServerConfigurer,
				(authorizationSever) -> authorizationSever.oidc(Customizer.withDefaults())
			)
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
			.exceptionHandling((exceptions) -> exceptions
				.defaultAuthenticationEntryPointFor(
						new LoginUrlAuthenticationEntryPoint(gatewayClientUrl + "/login"),
						new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
				)
			);
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers(whiteList).permitAll()
					.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2
					.loginPage("/login").failureUrl(gatewayClientUrl + "/login?error=true")
					.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)))
			.formLogin(form -> form
					.loginPage("/login").failureUrl(gatewayClientUrl + "/login?error=true"))
			.userDetailsService(customUserDetailService);

		return http.build();
	}

	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            clientUrl
        ));
        config.addAllowedMethod("*");
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}