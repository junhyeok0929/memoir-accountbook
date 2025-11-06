package com.memoir.accountbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignUpRequestDto {

    private String email;
    private String password;
    private String nickname;
}