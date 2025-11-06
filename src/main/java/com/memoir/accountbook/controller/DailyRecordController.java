package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.DailyRecordResponseDto;
import com.memoir.accountbook.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat; // import 구문 확인
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate; // import 구문 확인

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/daily-records")
public class DailyRecordController {

    private final DiaryService diaryService;

    @GetMapping("/member/{memberId}/date/{date}")
    public ResponseEntity<DailyRecordResponseDto> getDailyRecord(
            @PathVariable Long memberId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {


        DailyRecordResponseDto dailyRecord = diaryService.getDailyRecord(memberId, date);
        return ResponseEntity.ok(dailyRecord);
    }
}