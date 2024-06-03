package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "address")
@NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_addr", unique = true, nullable = false)
    private long idAddr;

    @Column(name = "apt_no", length = 255)
    private String aptNo;

    @Column(length = 255)
    private String city;

    @Column(length = 255)
    private String state;

    @Column(length = 255)
    private String street;

    @Column(length = 255)
    private String zip;

    @OneToMany(mappedBy = "address")
    private List<ChurchInformation> churchInformations;

    @OneToMany(mappedBy = "address")
    private List<Member> members;

    public Address() {
    }

    public long getIdAddr() {
        return this.idAddr;
    }

    public void setIdAddr(long idAddr) {
        this.idAddr = idAddr;
    }

    public String getAptNo() {
        return this.aptNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<ChurchInformation> getChurchInformations() {
        return this.churchInformations;
    }

    public void setChurchInformations(List<ChurchInformation> churchInformations) {
        this.churchInformations = churchInformations;
    }

    public ChurchInformation addChurchInformation(ChurchInformation churchInformation) {
        getChurchInformations().add(churchInformation);
        churchInformation.setAddress(this);

        return churchInformation;
    }

    public ChurchInformation removeChurchInformation(ChurchInformation churchInformation) {
        getChurchInformations().remove(churchInformation);
        churchInformation.setAddress(null);

        return churchInformation;
    }

    public List<Member> getMembers() {
        return this.members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Member addMember(Member member) {
        getMembers().add(member);
        member.setAddress(this);

        return member;
    }

    public Member removeMember(Member member) {
        getMembers().remove(member);
        member.setAddress(null);

        return member;
    }
}
