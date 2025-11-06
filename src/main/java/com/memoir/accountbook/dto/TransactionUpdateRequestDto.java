package com.memoir.accountbook.dto;

import com.memoir.accountbook.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TransactionUpdateRequestDto {

    // 수정할 수 있는 정보들
    private LocalDate transactionDate;
    private TransactionType type;
    private Integer amount;
    private String category;
    private String memo;
}