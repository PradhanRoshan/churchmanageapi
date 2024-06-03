package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.dto.PaymentMethodDTO;
import org.springframework.stereotype.Service;

@Service
public interface PaymentMethodService {
    String addPaymentMethod(PaymentMethodDTO paymentMethodDTO);
}
