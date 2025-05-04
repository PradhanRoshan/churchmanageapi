package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "application_status_history")
@NamedQuery(name = "ApplicationStatusHistory.findAll", query = "SELECT a FROM ApplicationStatusHistory a")
public class ApplicationStatusHistory extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Size(max = 50)
    @NotNull
    @Column(name = "application_type", nullable = false, length = 50)
    private String applicationType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "application_status_id", nullable = false)
    private ApplicationStatus applicationStatus;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;


}