package com.memoir.accountbook.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long validityInMilliseconds = 3600000; // 1시간

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // [수정 완료] 'Claims' 오류를 수정한 createToken 메소드
    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email) // 토큰의 '주인'(이메일)을 직접 설정
                .setIssuedAt(now) // 발급 시간
                .setExpiration(validity) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 비밀 키로 서명
                .compact(); // 문자열로 만듦
    }


    // --- ▼▼▼ 'parserBuilder'를 'parser'로 수정한 최종본 ▼▼▼ ---

    // 토큰에서 '주인'(이메일) 정보 꺼내기
    public String getEmail(String token) {
        // Jwts.parser()를 사용하고, verifyWith(key)로 검증합니다.
        // parseSignedClaims(token)을 사용하고 getPayload()로 내용을 가져옵니다.
        return Jwts.parser()
                .verifyWith(key) // 1. 비밀 키로 서명 검증
                .build()
                .parseSignedClaims(token) // 2. 토큰 파싱
                .getPayload() // 3. 내용물(Claims) 꺼내기
                .getSubject(); // 4. '주인'(이메일) 정보 반환
    }

    // 토큰이 유효한지(만료되지 않았는지, 서명이 올바른지) 검증
    public boolean validateToken(String token) {
        try {
            // getEmail과 똑같은 로직을 실행. 성공하면 true, 실패(예외 발생)하면 false
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return !claims.getPayload().getExpiration().before(new Date()); // 만료 시간도 한번 더 체크
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않으면(만료, 위조, 형식 오류 등) false 반환
            return false;
        }
    }
    // --- ▲▲▲ 'parserBuilder'를 'parser'로 수정한 최종본 ▲▲▲ ---
}