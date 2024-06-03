package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.FundType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundTypeRepository extends JpaRepository<FundType, Long> {
}