package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.TitheAndOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitheAndOfferingRepository extends JpaRepository<TitheAndOffering, Long> {
}