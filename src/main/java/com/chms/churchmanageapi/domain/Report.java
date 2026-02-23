package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reports")
@NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r")
public class Report extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", unique = true, nullable = false)
    private long reportId;

    @Column(name = "report_type", length = 255, nullable = false)
    private String reportType;

    @ManyToOne
    @JoinColumn(name = "generated_by", nullable = false)
    private User generatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "generated_date", nullable = false)
    private Date generatedDate;

    @Column(name = "content", length = 255, nullable = false)
    private String content;

    @Temporal(TemporalType.DATE)
    @Column(name = "report_exptn", nullable = true)
    private Date reportExptn;

    public Report() {
    }

    // Getters and setters

}
