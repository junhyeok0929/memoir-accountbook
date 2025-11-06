package com.memoir.accountbook.dto;

import com.memoir.accountbook.Diary;
import lombok.Getter;

import java.util.List;

@Getter
public class DailyRecordResponseDto {

    // '거래 내역 목록'을 담을 필드
    private List<TransactionResponseDto> transactions;

    // '일기'를 담을 필드 (하루에 일기는 하나라고 가정)
    private DiaryResponseDto diary;

    // 생성자: 서비스에서 DTO 목록들을 받아서 채워줍니다.
    public DailyRecordResponseDto(List<TransactionResponseDto> transactions, DiaryResponseDto diary) {
        this.transactions = transactions;
        this.diary = diary;
    }
}