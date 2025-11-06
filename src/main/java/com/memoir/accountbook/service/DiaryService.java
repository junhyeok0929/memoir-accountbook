package com.memoir.accountbook.service;

// 기존 import 구문들
import com.memoir.accountbook.Diary;
import com.memoir.accountbook.Member;
import com.memoir.accountbook.dto.DiaryCreateRequestDto;
import com.memoir.accountbook.dto.DiaryResponseDto;
import com.memoir.accountbook.repository.DiaryRepository;
import com.memoir.accountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// ---------------------------------------------
// ▼▼▼ '통합 조회' 기능을 위해 새로 추가된 import 구문들 ▼▼▼
// ---------------------------------------------
import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.dto.DailyRecordResponseDto;
import com.memoir.accountbook.dto.TransactionResponseDto;
import com.memoir.accountbook.repository.TransactionRepository;
import java.time.LocalDate;
import java.util.Optional;
// ---------------------------------------------
// ▲▲▲ 여기까지가 새로 추가된 import 구문들입니다 ▲▲▲
// ---------------------------------------------

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    // ▼▼▼ '통합 조회' 기능을 위해 TransactionRepository 주입 추가 ▼▼▼
    private final TransactionRepository transactionRepository;

    // --- 기존 일기 생성 메소드 (수정 없음) ---
    @Transactional
    public void createDiary(DiaryCreateRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + requestDto.getMemberId()));

        Diary diary = Diary.builder()
                .member(member)
                .diaryDate(requestDto.getDiaryDate())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .emotion(requestDto.getEmotion())
                .photoUrl(requestDto.getPhotoUrl())
                .build();

        diaryRepository.save(diary);
    }

    // --- 기존 일기 목록 조회 메소드 (수정 없음) ---
    @Transactional(readOnly = true)
    public List<DiaryResponseDto> findDiariesByMemberId(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + memberId));

        List<Diary> diaries = diaryRepository.findByMember_Id(memberId);

        return diaries.stream()
                .map(DiaryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DailyRecordResponseDto getDailyRecord(Long memberId, LocalDate date) {

        // 1. TransactionRepository를 사용해 특정 날짜의 '거래 내역 목록'을 찾습니다.
        List<Transaction> transactions = transactionRepository.findByMember_IdAndTransactionDate(memberId, date);

        // 2. 찾아온 거래 내역 목록을 DTO 목록으로 변환합니다.
        List<TransactionResponseDto> transactionDtos = transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());

        // 3. DiaryRepository를 사용해 특정 날짜의 '일기'를 찾습니다.
        Optional<Diary> optionalDiary = diaryRepository.findByMember_IdAndDiaryDate(memberId, date);

        // 4. 찾아온 일기(Optional)를 DTO로 변환합니다. (일기가 없으면 null이 됨)
        DiaryResponseDto diaryDto = optionalDiary
                .map(DiaryResponseDto::new) // 일기가 존재하면 DTO로 변환
                .orElse(null); // 일기가 존재하지 않으면 null

        // 5. 변환된 DTO 목록과 DTO 객체를 '통합 DTO'에 담아 반환합니다.
        return new DailyRecordResponseDto(transactionDtos, diaryDto);
    }
}