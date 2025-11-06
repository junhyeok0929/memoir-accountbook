package com.memoir.accountbook.jwt;

// --- ▼▼▼ 필요한 모든 import 구문입니다 ▼▼▼ ---
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
// --- ▲▲▲ 여기까지가 import 구문입니다 ▲▲▲ ---

@Component // 스프링이 이 클래스를 관리하도록 등록
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 모든 요청마다 한 번씩 실행되는 필터

    private final JwtTokenProvider jwtTokenProvider; // 우리가 만든 '출입증 발급기'

    // 이 필터가 실제 '검문'을 수행하는 메소드
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청에서 '출입증(JWT)'을 꺼냅니다.
        String token = resolveToken(request);

        // 2. '출입증'이 존재하고 유효한지 검사합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 출입증이 유효하면, 출입증에서 '주인'(이메일) 정보를 꺼냅니다.
            String email = jwtTokenProvider.getEmail(token);

            // 4. 보안 시스템에 "이 사용자는 인증되었습니다"라고 등록합니다.
            // (이메일을 이름으로, 비밀번호는 null, 권한은 없음(Collections.emptyList())으로 설정)
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 다음 검문소(필터)로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }

    // HTTP 요청 헤더에서 'Authorization'을 찾아 'Bearer ' 부분을 떼어내고 토큰만 반환하는 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " (7글자) 이후의 문자열을 반환
        }
        return null;
    }
}