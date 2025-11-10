package com.memoir.accountbook.config;

import com.memoir.accountbook.jwt.JwtAuthenticationFilter; // <--- [추가!] 우리가 만든 필터 import
import com.memoir.accountbook.jwt.JwtTokenProvider; // <--- [추가!] JWT 발급기 import
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // <--- [추가!] 필터 순서를 위해 import

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // --- ▼▼▼ [추가!] 우리가 만든 JwtAuthenticationFilter를 주입받습니다 ▼▼▼ ---
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF, 세션, Iframe 설정 (기존과 동일)
        http.csrf((csrf) -> csrf.disable());
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.headers((headers) -> headers
                .frameOptions((frameOptions) -> frameOptions.disable())
        );

        // 2. API 경로별 접근 권한 설정 (기존과 동일)
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS Preflight 요청 허용
                .requestMatchers("/api/members/signup").permitAll()
                .requestMatchers("/api/members/login").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 3. 비밀번호 암호화 도구 (기존과 동일)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}