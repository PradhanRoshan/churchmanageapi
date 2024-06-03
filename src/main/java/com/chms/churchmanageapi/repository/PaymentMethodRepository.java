package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByPaymentMethodNameIgnoreCase(String paymentMethodName);
}