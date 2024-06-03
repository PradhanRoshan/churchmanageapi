package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}