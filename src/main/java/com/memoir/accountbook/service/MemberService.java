package com.memoir.accountbook.service;

import com.memoir.accountbook.Member;
import com.memoir.accountbook.dto.MemberSignUpRequestDto;
import com.memoir.accountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(MemberSignUpRequestDto requestDto) {

        // 아래 코드로 변경해주세요.
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .nickname(requestDto.getNickname())
                .build();

        memberRepository.save(member);
    }
}