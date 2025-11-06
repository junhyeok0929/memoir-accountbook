package com.memoir.accountbook.util;

// --- ▼▼▼ 필요한 import 구문입니다 ▼▼▼ ---
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
// --- ▲▲▲ import 구문입니다 ▲▲▲ ---

public class SecurityUtil {

    // SecurityContextHolder에서 현재 인증된 사용자의 '이메일'을 꺼내는 메소드
    public static String getCurrentUserEmail() {
        // 1. '보안 관리실(SecurityContextHolder)'에서 '인증 정보(Authentication)'를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 인증 정보가 없거나, 인증되지 않았다면 예외를 발생시킵니다.
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("보안 컨텍스트에 인증 정보가 없습니다.");
        }

        // 3. 인증 정보에서 사용자의 '이름(principal)'을 반환합니다.
        //    (우리가 JwtAuthenticationFilter에서 이메일을 이름으로 저장했기 때문에,
        //     authentication.getName()은 곧 사용자의 '이메일'입니다.)
        return authentication.getName();
    }
}