package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Long> {
}