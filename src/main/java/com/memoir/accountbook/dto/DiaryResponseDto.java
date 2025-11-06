package com.memoir.accountbook.dto;

import com.memoir.accountbook.Diary;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class DiaryResponseDto {

    private Long diaryId;
    private LocalDate diaryDate;
    private String title;
    private String content;
    private String emotion;
    private String photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Diary 엔티티를 받아서 DTO로 변환하는 생성자
    public DiaryResponseDto(Diary diary) {
        this.diaryId = diary.getId();
        this.diaryDate = diary.getDiaryDate();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.emotion = diary.getEmotion();
        this.photoUrl = diary.getPhotoUrl();
        this.createdAt = diary.getCreatedAt();
        this.updatedAt = diary.getUpdatedAt();
    }
}