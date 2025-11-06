package com.memoir.accountbook;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String category;

    private String memo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // --- ▼▼▼ 이 update 메소드를 통째로 추가해주세요 ▼▼▼ ---
    public void update(LocalDate transactionDate, TransactionType type, Integer amount, String category, String memo) {
        this.transactionDate = transactionDate;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.memo = memo;
    }
}