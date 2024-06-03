package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}