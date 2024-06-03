package com.chms.churchmanageapi.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.Budget}
 */
@Data
public class BudgetDTO implements Serializable {
   private long budgetId;
   private int year;
   private double amount;
   private Double allocatedAmount;
}