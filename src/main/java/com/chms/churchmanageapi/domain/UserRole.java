package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the user_roles database table.
 *
 */
@Entity
@Table(name="user_roles")
@NamedQuery(name="UserRole.findAll", query="SELECT u FROM UserRole u")
public class UserRole extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserRolePK id;

    @Temporal(TemporalType.DATE)
    @Column(name = "user_role_exptn", nullable = true)
    private Date userRoleExptn;

    //bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name="role_id", nullable=false, insertable=false, updatable=false)
    private User user;

    //bi-directional many-to-one association to Role
    @ManyToOne
    @JoinColumn(name="role_id", nullable=false, insertable=false, updatable=false)
    private Role role;

    public UserRole() {
    }

    public UserRolePK getId() {
        return this.id;
    }

    public void setId(UserRolePK id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getUserRoleExptn() {
        return userRoleExptn;
    }

    public void setUserRoleExptn(Date userRoleExptn) {
        this.userRoleExptn = userRoleExptn;
    }
}