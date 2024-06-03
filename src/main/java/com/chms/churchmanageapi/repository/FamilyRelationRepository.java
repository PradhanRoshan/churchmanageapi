package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.FamilyRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRelationRepository extends JpaRepository<FamilyRelation, Long> {
}