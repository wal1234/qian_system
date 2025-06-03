package com.qian.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF 保护
            .csrf(csrf -> csrf.disable())
            // 允许所有请求
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            // 禁用表单登录
            .formLogin(form -> form.disable())
            // 禁用 HTTP Basic 认证
            .httpBasic(basic -> basic.disable());
            
        return http.build();
    }
} 