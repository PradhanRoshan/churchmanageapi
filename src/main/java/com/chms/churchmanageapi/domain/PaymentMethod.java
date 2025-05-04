package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "payment_method")
@NamedQuery(name = "PaymentMethod.findAll", query = "SELECT p FROM PaymentMethod p")
public class PaymentMethod extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id", unique = true, nullable = false)
    private long paymentMethodId;

    @Column(name = "payment_method_name", length = 255, nullable = false)
    private String paymentMethodName;

    @OneToMany(mappedBy = "paymentMethod")
    private List<TitheAndOffering> titheAndOfferings;

    public PaymentMethod() {
    }

    // Getters and setters


    public long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public List<TitheAndOffering> getTitheAndOfferings() {
        return titheAndOfferings;
    }

    public void setTitheAndOfferings(List<TitheAndOffering> titheAndOfferings) {
        this.titheAndOfferings = titheAndOfferings;
    }
}
