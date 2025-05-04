package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.UserRole;
import com.chms.churchmanageapi.domain.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
    @Query("SELECT r FROM UserRole r WHERE r.id.userId = :userId AND r.userRoleExptn IS NULL")
    Optional<UserRole> findByIdUserIdAndUserRoleExptnIsNull(@Param("userId") long userId);


    Optional<UserRole> findById_UserIdAndId_RoleIdAndUserRoleExptnIsNull(long userId, long roleId);
}