package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.domain.PaymentMethod;
import com.chms.churchmanageapi.dto.PaymentMethodDTO;
import com.chms.churchmanageapi.repository.PaymentMethodRepository;
import com.chms.churchmanageapi.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public String addPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        Optional<PaymentMethod> opt = paymentMethodRepository.findByPaymentMethodNameIgnoreCase(paymentMethodDTO.getPaymentMethodName());
        if(opt.isPresent()){
            return "Payment name is already present";
        }
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethodName(paymentMethodDTO.getPaymentMethodName());
        paymentMethodRepository.save(paymentMethod);
        return "Success";
    }
}
