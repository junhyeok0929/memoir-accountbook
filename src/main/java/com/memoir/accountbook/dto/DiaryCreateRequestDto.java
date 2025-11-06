package com.memoir.accountbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DiaryCreateRequestDto {

    private LocalDate diaryDate;
    private String title;
    private String content;
    private String emotion;
    private String photoUrl; // 사진 URL은 일단 문자열로 저장 (추후 파일 업로드 기능 추가 가능)
}