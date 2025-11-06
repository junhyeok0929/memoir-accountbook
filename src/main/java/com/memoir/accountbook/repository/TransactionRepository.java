package com.memoir.accountbook.repository;
import java.util.List;
import com.memoir.accountbook.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByMember_Id(Long memberId);
    List<Transaction> findByMember_IdAndTransactionDate(Long memberId, LocalDate date);
}