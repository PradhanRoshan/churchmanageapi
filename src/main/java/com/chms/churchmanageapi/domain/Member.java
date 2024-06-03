package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "members")
@NamedQuery(name = "Member.findAll", query = "SELECT m FROM Member m")
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "member_id", unique = true, nullable = false, length = 8)
    private String memberId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true)
    private User user;

    @Column(name = "email_id", length = 255, nullable = true)
    private String emailId;

    @Column(name = "first_name", length = 255, nullable = false)
    private String firstName;

    @Column(name = "gender", length = 255, nullable = false)
    private String gender;

    @Column(name = "last_name", length = 255, nullable = false)
    private String lastName;

    @Column(name = "marital_status", length = 255, nullable = false)
    private String maritalStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "member_dob", nullable = false)
    private Date memberDob;

    @Temporal(TemporalType.DATE)
    @Column(name = "member_exptn", nullable = true)
    private Date memberExptn;

    @Column(name = "middle_name", length = 255, nullable = true)
    private String middleName;

    @Column(name = "phone", length = 255, nullable = false)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "id_addr", referencedColumnName = "id_addr", nullable = true)
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<FamilyRelation> familyRelations;

    @OneToMany(mappedBy = "member")
    private List<TitheAndOffering> titheAndOfferings;

    @OneToMany(mappedBy = "member")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "volunteer")
    private List<VolunteerActivity> volunteerActivities;

    public Member() {
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Date getMemberDob() {
        return memberDob;
    }

    public void setMemberDob(Date memberDob) {
        this.memberDob = memberDob;
    }

    public Date getMemberExptn() {
        return memberExptn;
    }

    public void setMemberExptn(Date memberExptn) {
        this.memberExptn = memberExptn;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<FamilyRelation> getFamilyRelations() {
        return familyRelations;
    }

    public void setFamilyRelations(List<FamilyRelation> familyRelations) {
        this.familyRelations = familyRelations;
    }

    public List<TitheAndOffering> getTitheAndOfferings() {
        return titheAndOfferings;
    }

    public void setTitheAndOfferings(List<TitheAndOffering> titheAndOfferings) {
        this.titheAndOfferings = titheAndOfferings;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    public List<VolunteerActivity> getVolunteerActivities() {
        return volunteerActivities;
    }

    public void setVolunteerActivities(List<VolunteerActivity> volunteerActivities) {
        this.volunteerActivities = volunteerActivities;
    }
}
