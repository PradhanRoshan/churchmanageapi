package com.chms.churchmanageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.PaymentMethod}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDTO {
    private long paymentMethodId;
    private String paymentMethodName;
}