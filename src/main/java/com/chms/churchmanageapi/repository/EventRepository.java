package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}