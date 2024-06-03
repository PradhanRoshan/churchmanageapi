package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}