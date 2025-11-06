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
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));



        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        String accessToken = jwtTokenProvider.createToken(member.getEmail());
        return new TokenResponseDto(accessToken);
    }
}