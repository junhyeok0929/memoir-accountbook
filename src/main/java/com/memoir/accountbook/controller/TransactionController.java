package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.TransactionCreateRequestDto;
import com.memoir.accountbook.dto.TransactionResponseDto;
import com.memoir.accountbook.dto.TransactionUpdateRequestDto;
import com.memoir.accountbook.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // --- 1. 거래 내역 생성 (C) (수정 없음) ---
    // (DTO에서 memberId가 빠졌지만, Controller는 Service를 호출할 뿐이라 수정할 게 없습니다!)
    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionCreateRequestDto requestDto) {
        transactionService.createTransaction(requestDto);
        return ResponseEntity.ok().build();
    }

    // --- 2. '내' 거래 내역 조회 (R) (API 주소 및 파라미터 변경!) ---
    // [수정됨!] 주소가 /member/{memberId} -> /me 로 변경
    @GetMapping("/me")
    // [수정됨!] @PathVariable Long memberId 파라미터 삭제
    public ResponseEntity<List<TransactionResponseDto>> findMyTransactions() {
        // [수정됨!] 서비스의 새로운 메소드 호출
        List<TransactionResponseDto> transactions = transactionService.findMyTransactions();
        return ResponseEntity.ok(transactions);
    }

    // --- 3. 거래 내역 수정 (U) (수정 없음) ---
    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody TransactionUpdateRequestDto requestDto) {

        transactionService.updateTransaction(transactionId, requestDto);
        return ResponseEntity.ok().build();
    }

    // --- 4. 거래 내역 삭제 (D) (수정 없음) ---
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {

        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }
}