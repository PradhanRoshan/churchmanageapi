package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "budgets")
@NamedQuery(name = "Budget.findAll", query = "SELECT b FROM Budget b")
public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id", unique = true, nullable = false)
    private long budgetId;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "allocated_amount", nullable = true)
    private Double allocatedAmount;

    public Budget() {
    }

    public long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(long budgetId) {
        this.budgetId = budgetId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Double getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(Double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }
}
