package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "volunteer_activities")
@NamedQuery(name = "VolunteerActivity.findAll", query = "SELECT v FROM VolunteerActivity v")
public class VolunteerActivity extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id", unique = true, nullable = false)
    private long activityId;

    @Column(name = "activity_name", length = 255, nullable = false)
    private String activityName;

    @Temporal(TemporalType.DATE)
    @Column(name = "activity_date", nullable = false)
    private Date activityDate;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = true)
    private Member volunteer;

    public VolunteerActivity() {
    }

    // Getters and setters

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Member getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Member volunteer) {
        this.volunteer = volunteer;
    }
}
