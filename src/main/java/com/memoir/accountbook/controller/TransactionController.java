package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.TransactionCreateRequestDto;
import com.memoir.accountbook.dto.TransactionResponseDto; // Response DTO import 확인!
import com.memoir.accountbook.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // GetMapping, PathVariable import 위해 * 로 변경 확인!
import com.memoir.accountbook.dto.TransactionUpdateRequestDto; // 수정 DTO import 추가!
import org.springframework.web.bind.annotation.PutMapping; // PutMapping import 추가!
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List; // List import 확인!

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // 거래 내역 생성 API (POST /api/transactions)
    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionCreateRequestDto requestDto) {
        transactionService.createTransaction(requestDto);
        return ResponseEntity.ok().build();
    }

    // 특정 회원의 거래 내역 목록 조회 API (GET /api/transactions/member/{memberId})
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<TransactionResponseDto>> findTransactionsByMemberId(@PathVariable Long memberId) {
        List<TransactionResponseDto> transactions = transactionService.findTransactionsByMemberId(memberId);
        return ResponseEntity.ok(transactions); // 조회된 목록을 응답 본문에 담아 반환
    }

    @PutMapping("/{transactionId}") // PUT 방식으로 /api/transactions/{거래ID} 요청 처리
    public ResponseEntity<Void> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody TransactionUpdateRequestDto requestDto) {

        transactionService.updateTransaction(transactionId, requestDto);
        return ResponseEntity.ok().build(); // 수정 성공 응답 (200 OK)
    }

    @DeleteMapping("/{transactionId}") // DELETE 방식으로 /api/transactions/{거래ID} 요청 처리
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {

        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build(); // 삭제 성공 응답 (200 OK)
    }
}