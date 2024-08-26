package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.VolunteerActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerActivityRepository extends JpaRepository<VolunteerActivity, Long> {
}