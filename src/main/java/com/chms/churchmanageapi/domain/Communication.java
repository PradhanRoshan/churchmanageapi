package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "communications")
@NamedQuery(name = "Communication.findAll", query = "SELECT c FROM Communication c")
public class Communication implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "communication_id", unique = true, nullable = false)
    private long communicationId;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "message", length = 255, nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_date", nullable = false)
    private Date sentDate;

    public Communication() {
    }

    // Getters and setters

    public long getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(long communicationId) {
        this.communicationId = communicationId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
