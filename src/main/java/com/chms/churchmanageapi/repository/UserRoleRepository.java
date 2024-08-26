package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.UserRole;
import com.chms.churchmanageapi.domain.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
}