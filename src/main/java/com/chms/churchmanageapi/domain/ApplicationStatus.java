package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "application_status")
@NamedQuery(name = "ApplicationStatus.findAll", query = "SELECT a FROM ApplicationStatus a")
public class ApplicationStatus extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_status_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "status_name", nullable = false, length = 50)
    private String statusName;


    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "applicationStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationStatusHistory> applicationStatusHistories = new ArrayList<>();

    @OneToMany(mappedBy = "applicationStatus")
    private List<Member> members = new ArrayList<>();
}