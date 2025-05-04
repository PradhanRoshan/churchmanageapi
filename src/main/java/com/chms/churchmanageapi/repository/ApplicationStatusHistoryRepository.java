package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.ApplicationStatusHistory;
import com.chms.churchmanageapi.domain.Member;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Long> {
    List<ApplicationStatusHistory> findByMember(@NotNull Member member);
}