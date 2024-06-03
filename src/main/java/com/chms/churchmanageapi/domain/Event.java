package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "events")
@NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e")
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", unique = true, nullable = false)
    private long eventId;

    @Column(name = "event_name", length = 255, nullable = false)
    private String eventName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_date", nullable = false)
    private Date eventDate;

    @Column(name = "event_location", length = 255, nullable = false)
    private String eventLocation;

    @Column(name = "event_description", length = 255, nullable = true)
    private String eventDescription;

    @OneToMany(mappedBy = "event")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "event")
    private List<VolunteerActivity> volunteerActivities;

    public Event() {
    }

    // Getters and setters

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
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
