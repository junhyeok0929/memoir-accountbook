package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.DiaryCreateRequestDto;
import com.memoir.accountbook.dto.DiaryResponseDto;
import com.memoir.accountbook.dto.DiaryUpdateRequestDto; // <--- 수정 DTO import 추가!
import com.memoir.accountbook.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // GetMapping, PathVariable, DeleteMapping, PutMapping import 위해 * 로 변경!

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // --- 1. 일기 생성 (C) (기존 코드) ---
    @PostMapping
    public ResponseEntity<Void> createDiary(@RequestBody DiaryCreateRequestDto requestDto) {
        diaryService.createDiary(requestDto);
        return ResponseEntity.ok().build();
    }

    // --- 2. 일기 목록 조회 (R) (기존 코드) ---
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<DiaryResponseDto>> findDiariesByMemberId(@PathVariable Long memberId) {
        List<DiaryResponseDto> diaries = diaryService.findDiariesByMemberId(memberId);
        return ResponseEntity.ok(diaries);
    }

    // ---------------------------------------------
    // ▼▼▼ 3. 새로 추가된 '일기 수정' (U) API ▼▼▼
    // ---------------------------------------------
    @PutMapping("/{diaryId}") // PUT 방식으로 /api/diaries/{일기ID} 요청 처리
    public ResponseEntity<Void> updateDiary(
            @PathVariable Long diaryId,
            @RequestBody DiaryUpdateRequestDto requestDto) {

        diaryService.updateDiary(diaryId, requestDto);
        return ResponseEntity.ok().build(); // 수정 성공 응답 (200 OK)
    }

    // ---------------------------------------------
    // ▼▼▼ 4. 새로 추가된 '일기 삭제' (D) API ▼▼▼
    // ---------------------------------------------
    @DeleteMapping("/{diaryId}") // DELETE 방식으로 /api/diaries/{일기ID} 요청 처리
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {

        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build(); // 삭제 성공 응답 (200 OK)
    }
}