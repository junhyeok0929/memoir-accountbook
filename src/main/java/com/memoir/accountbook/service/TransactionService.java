package com.memoir.accountbook.service;

import com.memoir.accountbook.Member;
import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.dto.TransactionCreateRequestDto;
import com.memoir.accountbook.dto.TransactionResponseDto;
import com.memoir.accountbook.dto.TransactionUpdateRequestDto; // <--- 수정 DTO import 확인!
import com.memoir.accountbook.repository.MemberRepository;
import com.memoir.accountbook.repository.TransactionRepository;
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

    // --- 1. 거래 내역 생성 메소드 (기존 코드) ---
    @Transactional
    public void createTransaction(TransactionCreateRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + requestDto.getMemberId()));

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

    // --- 2. 회원 ID로 거래 내역 조회 메소드 (기존 코드) ---
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> findTransactionsByMemberId(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + memberId));

        List<Transaction> transactions = transactionRepository.findByMember_Id(memberId);

        return transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------
    // ▼▼▼ 3. 새로 추가된 '거래 내역 수정' 메소드 ▼▼▼
    // ---------------------------------------------
    @Transactional
    public void updateTransaction(Long transactionId, TransactionUpdateRequestDto requestDto) {

        // 1. 데이터베이스에서 수정할 원본 거래 내역을 찾습니다.
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("해D:devaccountbookaccountbook 해당 거래 내역을 찾을 수 없습니다. id=" + transactionId));

                        // 2. 찾아온 엔티티 객체의 update 메소드를 호출하여 값 변경
                        // (이 update 메소드는 Transaction 엔티티 클래스 안에 만들어 뒀습니다!)
                        transaction.update(
                                requestDto.getTransactionDate(),
                                requestDto.getType(),
                                requestDto.getAmount(),
                                requestDto.getCategory(),
                                requestDto.getMemo()
                        );

        // 3. @Transactional의 '변경 감지' 기능으로 자동 저장이 되므로,
        //    save()를 호출할 필요가 없습니다.
    }
    @Transactional // (데이터 변경이 있으므로 readOnly 아님!)
    public void deleteTransaction(Long transactionId) {

        // 1. 데이터베이스에서 삭제할 원본 거래 내역이 있는지 먼저 확인합니다.
        //    (없으면 orElseThrow가 실행되어 "찾을 수 없습니다" 오류를 반환)
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래 내역을 찾을 수 없습니다. id=" + transactionId));

        // 2. 찾아온 엔티티 객체를 Repository를 통해 삭제합니다.
        transactionRepository.delete(transaction);

        // (JPA의 '변경 감지'와 달리, .delete()는 명시적으로 호출해줘야 합니다.)
    }
}