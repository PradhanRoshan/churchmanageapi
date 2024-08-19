package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "church_information")
@NamedQuery(name = "ChurchInformation.findAll", query = "SELECT c FROM ChurchInformation c")
public class ChurchInformation extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "church_id", unique = true, nullable = false)
    private long churchId;

    @Column(name = "chur_denomination", length = 255, nullable = false)
    private String churDenomination;

    @Column(name = "chur_email", length = 255, nullable = false)
    private String churEmail;

    @Column(name = "chur_name", length = 255, nullable = false)
    private String churName;

    @Column(name = "chur_phone", length = 255, nullable = false)
    private String churPhone;

    @Column(name = "chur_website", length = 255, nullable = false)
    private String churWebsite;

    @Temporal(TemporalType.DATE)
    @Column(name = "church_exptn", nullable = true)
    private java.util.Date churchExptn;

    @ManyToOne
    @JoinColumn(name = "id_addr")
    private Address address;

    public ChurchInformation() {
    }

    // Getters and setters

    public long getChurchId() {
        return churchId;
    }

    public void setChurchId(long churchId) {
        this.churchId = churchId;
    }

    public String getChurDenomination() {
        return churDenomination;
    }

    public void setChurDenomination(String churDenomination) {
        this.churDenomination = churDenomination;
    }

    public String getChurEmail() {
        return churEmail;
    }

    public void setChurEmail(String churEmail) {
        this.churEmail = churEmail;
    }

    public String getChurName() {
        return churName;
    }

    public void setChurName(String churName) {
        this.churName = churName;
    }

    public String getChurPhone() {
        return churPhone;
    }

    public void setChurPhone(String churPhone) {
        this.churPhone = churPhone;
    }

    public String getChurWebsite() {
        return churWebsite;
    }

    public void setChurWebsite(String churWebsite) {
        this.churWebsite = churWebsite;
    }

    public Date getChurchExptn() {
        return churchExptn;
    }

    public void setChurchExptn(Date churchExptn) {
        this.churchExptn = churchExptn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
