package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}