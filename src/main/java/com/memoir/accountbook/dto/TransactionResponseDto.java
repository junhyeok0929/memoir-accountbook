package com.memoir.accountbook.dto;

import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.TransactionType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TransactionResponseDto {

    private Long transactionId;
    private LocalDate transactionDate;
    private TransactionType type;
    private Integer amount;
    private String category;
    private String memo;
    private LocalDateTime createdAt;

    // Transaction 엔티티를 받아서 DTO로 변환하는 생성자
    public TransactionResponseDto(Transaction transaction) {
        this.transactionId = transaction.getId();
        this.transactionDate = transaction.getTransactionDate();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.category = transaction.getCategory();
        this.memo = transaction.getMemo();
        this.createdAt = transaction.getCreatedAt();
    }
}