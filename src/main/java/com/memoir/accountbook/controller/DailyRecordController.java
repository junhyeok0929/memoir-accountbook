package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.DailyRecordResponseDto;
import com.memoir.accountbook.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/daily-records")
public class DailyRecordController {

    private final DiaryService diaryService;

    // --- '내' 일일 통합 조회 (R) (API 주소 및 파라미터 변경!) ---
    // [수정됨!] 주소가 /member/{memberId}/date/{date} -> /date/{date} 로 변경
    @GetMapping("/date/{date}")
    // [수정됨!] @PathVariable Long memberId 파라미터 삭제
    public ResponseEntity<DailyRecordResponseDto> getMyDailyRecord(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // [수정됨!] 서비스의 새로운 메소드 호출
        DailyRecordResponseDto dailyRecord = diaryService.getMyDailyRecord(date);
        return ResponseEntity.ok(dailyRecord);
    }
}