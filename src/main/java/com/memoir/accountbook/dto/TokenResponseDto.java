package com.memoir.accountbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 토큰을 받아 바로 객체를 생성하도록 합니다.
public class TokenResponseDto {
    private String accessToken;
}