package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.UserRole;
import com.chms.churchmanageapi.domain.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
    @Query("SELECT r FROM UserRole r WHERE r.id.userId = :userId AND r.userRoleExptn is null")
    UserRole findById_UserIdAndUserRoleExptnIsNull(long userId);
}