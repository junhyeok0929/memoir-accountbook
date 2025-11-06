package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.DiaryCreateRequestDto;
import com.memoir.accountbook.dto.DiaryResponseDto; // Response DTO import 확인!
import com.memoir.accountbook.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // GetMapping, PathVariable import 위해 * 로 변경 확인!

import java.util.List; // List import 확인!

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // 일기 생성 API (POST /api/diaries)
    @PostMapping
    public ResponseEntity<Void> createDiary(@RequestBody DiaryCreateRequestDto requestDto) {
        diaryService.createDiary(requestDto);
        return ResponseEntity.ok().build();
    }

    // 특정 회원의 일기 목록 조회 API (GET /api/diaries/member/{memberId})
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<DiaryResponseDto>> findDiariesByMemberId(@PathVariable Long memberId) {
        List<DiaryResponseDto> diaries = diaryService.findDiariesByMemberId(memberId);
        return ResponseEntity.ok(diaries); // 조회된 목록을 응답 본문에 담아 반환
    }
}