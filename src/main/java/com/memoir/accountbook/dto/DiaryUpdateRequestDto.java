package com.memoir.accountbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DiaryUpdateRequestDto {

    // 수정할 수 있는 정보들
    private LocalDate diaryDate;
    private String title;
    private String content;
    private String emotion;
    private String photoUrl;
}