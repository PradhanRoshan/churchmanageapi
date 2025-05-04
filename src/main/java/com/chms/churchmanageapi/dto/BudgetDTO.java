package com.chms.churchmanageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.Budget}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO implements Serializable {
   private long budgetId;
   private int year;
   private double amount;
   private Double allocatedAmount;
}