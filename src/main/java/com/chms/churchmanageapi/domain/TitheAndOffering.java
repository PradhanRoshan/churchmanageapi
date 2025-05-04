package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tithe_and_offering")
@NamedQuery(name = "TitheAndOffering.findAll", query = "SELECT t FROM TitheAndOffering t")
public class TitheAndOffering extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tithe_offering_id", unique = true, nullable = false)
    private long titheOfferingId;

    @Column(name = "amount_contributed", nullable = true)
    private double amountContributed;

    @Temporal(TemporalType.DATE)
    @Column(name = "tithe_offering_exptn", nullable = true)
    private Date titheOfferingExptn;

    @Temporal(TemporalType.DATE)
    @Column(name = "contributed_date", nullable = false)
    private Date contributedDate;

    @Column(name = "fund_note", length = 255, nullable = true)
    private String fundNote;

    @ManyToOne
    @JoinColumn(name = "fund_type_id", nullable = true)
    private FundType fundType;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = true)
    private PaymentMethod paymentMethod;

    public TitheAndOffering() {
    }

    // Getters and setters

    public long getTitheOfferingId() {
        return titheOfferingId;
    }

    public void setTitheOfferingId(long titheOfferingId) {
        this.titheOfferingId = titheOfferingId;
    }

    public double getAmountContributed() {
        return amountContributed;
    }

    public void setAmountContributed(double amountContributed) {
        this.amountContributed = amountContributed;
    }

    public Date getContributedDate() {
        return contributedDate;
    }

    public void setContributedDate(Date contributedDate) {
        this.contributedDate = contributedDate;
    }

    public String getFundNote() {
        return fundNote;
    }

    public void setFundNote(String fundNote) {
        this.fundNote = fundNote;
    }

    public FundType getFundType() {
        return fundType;
    }

    public void setFundType(FundType fundType) {
        this.fundType = fundType;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getTitheOfferingExptn() {
        return titheOfferingExptn;
    }

    public void setTitheOfferingExptn(Date titheOfferingExptn) {
        this.titheOfferingExptn = titheOfferingExptn;
    }
}
