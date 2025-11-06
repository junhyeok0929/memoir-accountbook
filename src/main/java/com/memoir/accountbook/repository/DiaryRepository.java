package com.memoir.accountbook.repository;

import com.memoir.accountbook.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMember_Id(Long memberId);
    Optional<Diary> findByMember_IdAndDiaryDate(Long memberId, LocalDate date);
}