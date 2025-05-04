package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.ChurchInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChurchInformationRepository extends JpaRepository<ChurchInformation, Long> {
}