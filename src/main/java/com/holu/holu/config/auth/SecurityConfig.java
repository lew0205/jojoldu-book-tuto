package com.holu.holu.config.auth;

import com.holu.holu.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(authorizationHttpRequests ->
                authorizationHttpRequests
                    .requestMatchers("/", "/css/**", "/images/**", "js/**", "/h2-console/**").permitAll()
                    .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated())
            .logout(logout ->
                logout.logoutSuccessUrl("/"))
            .oauth2Login(oauth2Login ->
                oauth2Login.userInfoEndpoint((userInfoEndpoint) ->
                    userInfoEndpoint.userService(customOAuth2UserService)));
        return http.build();
    }
}
