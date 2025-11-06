package com.memoir.accountbook.service;

import com.memoir.accountbook.Member;
import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.dto.TransactionCreateRequestDto;
import com.memoir.accountbook.dto.TransactionResponseDto;
import com.memoir.accountbook.dto.TransactionUpdateRequestDto;
import com.memoir.accountbook.repository.MemberRepository;
import com.memoir.accountbook.repository.TransactionRepository;
import com.memoir.accountbook.util.SecurityUtil; // <--- [필수!] SecurityUtil import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;

    // --- 1. 거래 내역 생성 (C) (지난번에 수정 완료!) ---
    @Transactional
    public void createTransaction(TransactionCreateRequestDto requestDto) {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 회원을 찾을 수 없습니다."));

        Transaction transaction = Transaction.builder()
                .member(member)
                .transactionDate(requestDto.getTransactionDate())
                .type(requestDto.getType())
                .amount(requestDto.getAmount())
                .category(requestDto.getCategory())
                .memo(requestDto.getMemo())
                .build();

        transactionRepository.save(transaction);
    }

    // --- 2. '본인'의 거래 내역 조회 (R) (파라미터가 바뀜!) ---
    @Transactional(readOnly = true)
    // [수정됨!] (Long memberId) 파라미터가 삭제되었습니다.
    public List<TransactionResponseDto> findMyTransactions() {
        // [수정됨!] SecurityUtil로 '현재 로그인한 사용자'의 이메일을 가져옵니다.
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 회원을 찾을 수 없습니다."));

        // [수정됨!] 파라미터 memberId가 아닌, 찾은 member 객체의 ID를 사용합니다.
        List<Transaction> transactions = transactionRepository.findByMember_Id(member.getId());

        return transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    // --- 3. '본인'의 거래 내역 수정 (U) (본인 확인 로직 추가!) ---
    @Transactional
    public void updateTransaction(Long transactionId, TransactionUpdateRequestDto requestDto) {
        // [추가!] 현재 로그인한 사용자의 이메일을 가져옵니다.
        String userEmail = SecurityUtil.getCurrentUserEmail();

        // 1. 수정할 거래 내역을 찾습니다.
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래 내역을 찾을 수 없습니다. id=" + transactionId));

        // 2. [★핵심 인가 로직★] 거래 내역의 주인(Member) 이메일과
        //    현재 로그인한 사용자의 이메일이 일치하는지 확인합니다.
        if (!transaction.getMember().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("본인의 거래 내역만 수정할 수 있습니다."); // 일치하지 않으면 예외 발생!
        }

        // 3. (검증 통과) 수정 로직 수행
        transaction.update(
                requestDto.getTransactionDate(),
                requestDto.getType(),
                requestDto.getAmount(),
                requestDto.getCategory(),
                requestDto.getMemo()
        );
    }

    // --- 4. '본인'의 거래 내역 삭제 (D) (본인 확인 로직 추가!) ---
    @Transactional
    public void deleteTransaction(Long transactionId) {
        // [추가!] 현재 로그인한 사용자의 이메일을 가져옵니다.
        String userEmail = SecurityUtil.getCurrentUserEmail();

        // 1. 삭제할 거래 내역을 찾습니다.
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래 내역을 찾을 수 없습니다. id=" + transactionId));

        // 2. [★핵심 인가 로직★] 거래 내역의 주인 이메일과
        //    현재 로그인한 사용자의 이메일이 일치하는지 확인합니다.
        if (!transaction.getMember().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("본인의 거래 내역만 삭제할 수 있습니다."); // 일치하지 않으면 예외 발생!
        }

        // 3. (검증 통과) 삭제 로직 수행
        transactionRepository.delete(transaction);
    }
}