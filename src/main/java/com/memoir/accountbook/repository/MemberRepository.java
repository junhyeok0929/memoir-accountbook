package com.memoir.accountbook.repository;

import com.memoir.accountbook.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}