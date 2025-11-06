package com.memoir.accountbook.service;

// --- 기존 import 구문들 ---
import com.memoir.accountbook.Diary;
import com.memoir.accountbook.Member;
import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.dto.*;
import com.memoir.accountbook.repository.DiaryRepository;
import com.memoir.accountbook.repository.MemberRepository;
import com.memoir.accountbook.repository.TransactionRepository;
import com.memoir.accountbook.util.SecurityUtil; // <--- [필수!] SecurityUtil import
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

    // --- 1. '내' 일기 생성 (C) (지난번에 수정 완료!) ---
    @Transactional
    public void createDiary(DiaryCreateRequestDto requestDto) {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 회원을 찾을 수 없습니다."));

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

    // --- 2. '내' 일기 목록 조회 (R) (파라미터 변경!) ---
    @Transactional(readOnly = true)
    // [수정됨!] (Long memberId) 파라미터가 삭제되었습니다.
    public List<DiaryResponseDto> findMyDiaries() {
        // [수정됨!] SecurityUtil로 '현재 로그인한 사용자'의 이메일을 가져옵니다.
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 회원을 찾을 수 없습니다."));

        // [수정됨!] 찾은 member 객체의 ID를 사용합니다.
        List<Diary> diaries = diaryRepository.findByMember_Id(member.getId());

        return diaries.stream()
                .map(DiaryResponseDto::new)
                .collect(Collectors.toList());
    }

    // --- 3. '내' 일일 통합 조회 (R) (파라미터 변경!) ---
    @Transactional(readOnly = true)
    // [수정됨!] (Long memberId) 파라미터가 삭제되었습니다.
    public DailyRecordResponseDto getMyDailyRecord(LocalDate date) {
        // [수정됨!] SecurityUtil로 '현재 로그인한 사용자'의 이메일을 가져옵니다.
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 회원을 찾을 수 없습니다."));

        // [수정됨!] 찾은 member 객체의 ID를 사용합니다.
        List<Transaction> transactions = transactionRepository.findByMember_IdAndTransactionDate(member.getId(), date);
        List<TransactionResponseDto> transactionDtos = transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());

        // [수정됨!] 찾은 member 객체의 ID를 사용합니다.
        Optional<Diary> optionalDiary = diaryRepository.findByMember_IdAndDiaryDate(member.getId(), date);
        DiaryResponseDto diaryDto = optionalDiary
                .map(DiaryResponseDto::new)
                .orElse(null);

        return new DailyRecordResponseDto(transactionDtos, diaryDto);
    }

    // --- 4. '내' 일기 수정 (U) (본인 확인 로직 추가!) ---
    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateRequestDto requestDto) {
        // [추가!] 현재 로그인한 사용자의 이메일을 가져옵니다.
        String userEmail = SecurityUtil.getCurrentUserEmail();

        // 1. 수정할 일기를 찾습니다.
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다. id=" + diaryId));

        // 2. [★핵심 인가 로직★] 일기의 주인 이메일과
        //    현재 로그인한 사용자의 이메일이 일치하는지 확인합니다.
        if (!diary.getMember().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("본인의 일기만 수정할 수 있습니다."); // 일치하지 않으면 예외 발생!
        }

        // 3. (검증 통과) 수정 로직 수행
        diary.update(
                requestDto.getDiaryDate(),
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getEmotion(),
                requestDto.getPhotoUrl()
        );
    }

    // --- 5. '내' 일기 삭제 (D) (본인 확인 로직 추가!) ---
    @Transactional
    public void deleteDiary(Long diaryId) {
        // [추가!] 현재 로그인한 사용자의 이메일을 가져옵니다.
        String userEmail = SecurityUtil.getCurrentUserEmail();

        // 1. 삭제할 일기를 찾습니다.
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기를 찾을 수 없습니다. id=" + diaryId));

        // 2. [★핵심 인가 로직★] 일기의 주인 이메일과
        //    현재 로그인한 사용자의 이메일이 일치하는지 확인합니다.
        if (!diary.getMember().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("본인의 일기만 삭제할 수 있습니다."); // 일치하지 않으면 예외 발생!
        }

        // 3. (검증 통과) 삭제 로직 수행
        diaryRepository.delete(diary);
    }
}