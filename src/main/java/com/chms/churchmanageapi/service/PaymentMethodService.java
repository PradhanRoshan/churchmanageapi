package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.domain.PaymentMethod;
import com.chms.churchmanageapi.dto.PaymentMethodDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentMethodService {
    String addPaymentMethod(PaymentMethodDTO paymentMethodDTO);

    List<PaymentMethodDTO> getPaymentMethodList();
}
