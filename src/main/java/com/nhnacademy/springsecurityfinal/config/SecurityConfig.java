package com.nhnacademy.springsecurityfinal.config;

import com.nhnacademy.springsecurityfinal.config.handler.LoginFailedHandler;
import com.nhnacademy.springsecurityfinal.config.handler.LoginSuccessHandler;
import com.nhnacademy.springsecurityfinal.config.handler.LogoutHandler;
import com.nhnacademy.springsecurityfinal.repository.RedisSecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailedHandler loginFailedHandler;
    private final LogoutHandler logoutHandler;
    private final RedisSecurityContextRepository redisSecurityContextRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(formLogin ->
            formLogin.loginPage("/auth/login")
                    .usernameParameter("id")
                    .passwordParameter("pwd")
                    .loginProcessingUrl("/auth/login/process")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailedHandler)
        );

        http.logout(logout ->
                logout.logoutSuccessHandler(logoutHandler)
        );

        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers(HttpMethod.GET, "/members/**").permitAll()
                                 .requestMatchers(HttpMethod.POST,"/signup").permitAll()
                                 .requestMatchers("/auth/login/**").permitAll()
                                 .requestMatchers("/admin/**").hasRole("ADMIN")
                                 .requestMatchers("/member/**").hasAuthority("ROLE_MEMBER")
                                 .requestMatchers("/google/**").hasRole("GOOGLE")
                                 .anyRequest().authenticated()
        );

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/403"));

        // TODO-Q 의존성 추가는 왜 작동 안할까
        http.securityContext(s->s.securityContextRepository(redisSecurityContextRepository));

        http.oauth2Login(oauth2 ->
                oauth2.loginPage("/auth/login")
                        .defaultSuccessUrl("/"));

        return http.build();
    }
}
