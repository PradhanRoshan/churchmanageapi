package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}