package com.memoir.accountbook;

import jakarta.persistence.*;
import lombok.*; // lombok.* 로 변경했습니다.

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

    @Lob // 내용을 길게 작성할 수 있도록 설정
    @Column(nullable = false)
    private String content;

    private String emotion;

    private String photoUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        // this.updatedAt = LocalDateTime.now(); // 필요하다면 이것도 추가할 수 있어요.
    }
}