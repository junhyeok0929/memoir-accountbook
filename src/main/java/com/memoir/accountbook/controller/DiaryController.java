package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.DiaryCreateRequestDto;
import com.memoir.accountbook.dto.DiaryResponseDto;
import com.memoir.accountbook.dto.DiaryUpdateRequestDto;
import com.memoir.accountbook.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // --- 1. '내' 일기 생성 (C) (수정 없음) ---
    @PostMapping
    public ResponseEntity<Void> createDiary(@RequestBody DiaryCreateRequestDto requestDto) {
        diaryService.createDiary(requestDto);
        return ResponseEntity.ok().build();
    }

    // --- 2. '내' 일기 목록 조회 (R) (API 주소 및 파라미터 변경!) ---
    // [수정됨!] 주소가 /member/{memberId} -> /me 로 변경
    @GetMapping("/me")
    // [수정됨!] @PathVariable Long memberId 파라미터 삭제
    public ResponseEntity<List<DiaryResponseDto>> findMyDiaries() {
        // [수정됨!] 서비스의 새로운 메소드 호출
        List<DiaryResponseDto> diaries = diaryService.findMyDiaries();
        return ResponseEntity.ok(diaries);
    }

    // --- 3. '내' 일기 수정 (U) (수정 없음) ---
    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @PathVariable Long diaryId,
            @RequestBody DiaryUpdateRequestDto requestDto) {

        diaryService.updateDiary(diaryId, requestDto);
        return ResponseEntity.ok().build();
    }

    // --- 4. '내' 일기 삭제 (D) (수정 없음) ---
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {

        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
}