package com.memoir.accountbook.service;

// --- 기존 import 구문들 ---
import com.memoir.accountbook.Diary;
import com.memoir.accountbook.Member;
import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.dto.*; // DTO가 많아졌으니 * 로 한번에 import
import com.memoir.accountbook.repository.DiaryRepository;
import com.memoir.accountbook.repository.MemberRepository;
import com.memoir.accountbook.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    // --- 1. 일기 생성 (C) (기존 코드) ---
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

    // --- 2. 일기 목록 조회 (R) (기존 코드) ---
    @Transactional(readOnly = true)
    public List<DiaryResponseDto> findDiariesByMemberId(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + memberId));

        List<Diary> diaries = diaryRepository.findByMember_Id(memberId);

        return diaries.stream()
                .map(DiaryResponseDto::new)
                .collect(Collectors.toList());
    }

    // --- 3. 일일 통합 조회 (R) (기존 코드) ---
    @Transactional(readOnly = true)
    public DailyRecordResponseDto getDailyRecord(Long memberId, LocalDate date) {
        List<Transaction> transactions = transactionRepository.findByMember_IdAndTransactionDate(memberId, date);

        List<TransactionResponseDto> transactionDtos = transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());

        Optional<Diary> optionalDiary = diaryRepository.findByMember_IdAndDiaryDate(memberId, date);

        DiaryResponseDto diaryDto = optionalDiary
                .map(DiaryResponseDto::new)
                .orElse(null);

        return new DailyRecordResponseDto(transactionDtos, diaryDto);
    }

    // ---------------------------------------------
    // ▼▼▼ 4. 새로 추가된 '일기 수정' (U) 메소드 ▼▼▼
    // ---------------------------------------------
    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateRequestDto requestDto) {
        // 1. 데이터베이스에서 수정할 원본 일기를 찾습니다.
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다. id=" + diaryId));

        // 2. 찾아온 엔티티 객체의 update 메소드를 호출하여 값 변경
        // (이 update 메소드는 Diary 엔티티 클래스 안에 만들어 뒀습니다!)
        diary.update(
                requestDto.getDiaryDate(),
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getEmotion(),
                requestDto.getPhotoUrl()
        );
        // 3. @Transactional의 '변경 감지' 기능으로 자동 저장 (save() 불필요)
    }

    // ---------------------------------------------
    // ▼▼▼ 5. 새로 추가된 '일기 삭제' (D) 메소드 ▼▼▼
    // ---------------------------------------------
    @Transactional
    public void deleteDiary(Long diaryId) {
        // 1. 데이터베이스에서 삭제할 원본 일기가 있는지 먼저 확인합니다.
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다. id=" + diaryId));

        // 2. 찾아온 엔티티 객체를 Repository를 통해 삭제합니다.
        diaryRepository.delete(diary);
    }
}