package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.FundType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundTypeRepository extends JpaRepository<FundType, Long> {
}