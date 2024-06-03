package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}