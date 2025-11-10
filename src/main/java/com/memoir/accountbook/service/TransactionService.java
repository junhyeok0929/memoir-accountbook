package com.memoir.accountbook.service;

import com.memoir.accountbook.Member;
import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.TransactionType;
import com.memoir.accountbook.dto.MonthlySummaryResponseDto;
import com.memoir.accountbook.dto.TransactionCreateRequestDto;
import com.memoir.accountbook.dto.TransactionResponseDto;
import com.memoir.accountbook.dto.TransactionUpdateRequestDto;
import com.memoir.accountbook.exception.CustomException;
import com.memoir.accountbook.exception.ErrorCode;
import com.memoir.accountbook.repository.MemberRepository;
import com.memoir.accountbook.repository.TransactionRepository;
import com.memoir.accountbook.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> findMyDailyTransactions(LocalDate date) {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Transaction> transactions = transactionRepository.findByMember_IdAndTransactionDate(member.getId(), date);

        return transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> findMyMonthlyTransactions(int year, int month) {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();

        List<Transaction> transactions = transactionRepository.findByMember_IdAndTransactionDateBetween(member.getId(), from, to);

        return transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MonthlySummaryResponseDto getMonthlySummary(int year, int month) {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();

        List<Transaction> transactions = transactionRepository.findByMember_IdAndTransactionDateBetween(member.getId(), from, to);

        long totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToLong(Transaction::getAmount)
                .sum();

        long totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToLong(Transaction::getAmount)
                .sum();

        Map<String, Long> incomeByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingLong(Transaction::getAmount)));

        Map<String, Long> expenseByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingLong(Transaction::getAmount)));

        return MonthlySummaryResponseDto.builder()
                .year(year)
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .incomeByCategory(incomeByCategory)
                .expenseByCategory(expenseByCategory)
                .build();
    }

    @Transactional
    public void createTransaction(TransactionCreateRequestDto requestDto) {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> findMyTransactions() {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Transaction> transactions = transactionRepository.findByMember_Id(member.getId());

        return transactions.stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionResponseDto findMyTransactionById(Long transactionId) {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return new TransactionResponseDto(transaction);
    }

    @Transactional
    public void updateTransaction(Long transactionId, TransactionUpdateRequestDto requestDto) {
        String userEmail = SecurityUtil.getCurrentUserEmail();

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getMember().getEmail().equals(userEmail)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        transaction.update(
                requestDto.getTransactionDate(),
                requestDto.getType(),
                requestDto.getAmount(),
                requestDto.getCategory(),
                requestDto.getMemo()
        );
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        String userEmail = SecurityUtil.getCurrentUserEmail();

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getMember().getEmail().equals(userEmail)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        transactionRepository.delete(transaction);
    }
}