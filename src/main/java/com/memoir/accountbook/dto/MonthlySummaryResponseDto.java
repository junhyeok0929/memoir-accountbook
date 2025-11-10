package com.memoir.accountbook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MonthlySummaryResponseDto {
    private int year;
    private int month;
    private long totalIncome;
    private long totalExpense;
    private Map<String, Long> incomeByCategory;
    private Map<String, Long> expenseByCategory;
}
