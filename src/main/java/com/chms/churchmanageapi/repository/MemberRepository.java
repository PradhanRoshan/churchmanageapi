package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}