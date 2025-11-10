// MemberService.java 파일입니다.

package com.memoir.accountbook.service;

// --- ▼▼▼ 필요한 import 구문들을 확인하거나 추가해주세요 ▼▼▼ ---
import com.memoir.accountbook.Member;
import com.memoir.accountbook.dto.LoginRequestDto;
import com.memoir.accountbook.dto.MemberSignUpRequestDto;
import com.memoir.accountbook.dto.TokenResponseDto;
import com.memoir.accountbook.jwt.JwtTokenProvider;
import com.memoir.accountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.memoir.accountbook.exception.CustomException;
import com.memoir.accountbook.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public void signUp(MemberSignUpRequestDto requestDto) {


        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto requestDto) {

        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));



        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        String accessToken = jwtTokenProvider.createToken(member.getEmail());
        return new TokenResponseDto(accessToken);
    }
}