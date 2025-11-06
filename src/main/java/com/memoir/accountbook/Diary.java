package com.memoir.accountbook;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDate diaryDate;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private String emotion;

    private String photoUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt; // <--- 수정 시간을 기록할 필드 (없었다면 추가)

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // --- ▼▼▼ 바로 이 메소드가 없어서 오류가 난 겁니다! ▼▼▼ ---
    // --- ▼▼▼ 이 메소드를 클래스 맨 아래에 추가해주세요! ▼▼▼ ---
    public void update(LocalDate diaryDate, String title, String content, String emotion, String photoUrl) {
        this.diaryDate = diaryDate;
        this.title = title;
        this.content = content;
        this.emotion = emotion;
        this.photoUrl = photoUrl;
        this.updatedAt = LocalDateTime.now(); // 수정 시간을 함께 기록합니다.
    }
}