package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "logs")
@NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l")
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", unique = true, nullable = false)
    private long logId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "action", length = 255, nullable = false)
    private String action;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "log_date", nullable = false)
    private Date logDate;

    public Log() {
    }

    // Getters and setters

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
}
