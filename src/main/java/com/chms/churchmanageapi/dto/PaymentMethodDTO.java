package com.chms.churchmanageapi.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.PaymentMethod}
 */
@Data
public class PaymentMethodDTO {
    private long paymentMethodId;
    private String paymentMethodName;
}