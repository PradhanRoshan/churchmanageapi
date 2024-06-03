package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Communication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunicationRepository extends JpaRepository<Communication, Long> {
}