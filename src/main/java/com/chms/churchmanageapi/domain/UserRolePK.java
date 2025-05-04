package com.chms.churchmanageapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

/**
 * The primary key class for the user_roles database table.
 *
 */
@Embeddable
public class UserRolePK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "role_id")
    private long roleId;

    public UserRolePK() {
    }
    public UserRolePK(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getRoleId() {
        return this.roleId;
    }
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserRolePK)) {
            return false;
        }
        UserRolePK castOther = (UserRolePK)other;
        return
                (this.userId == castOther.userId)
                        && (this.roleId == castOther.roleId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.userId ^ (this.userId >>> 32)));
        hash = hash * prime + ((int) (this.roleId ^ (this.roleId >>> 32)));

        return hash;
    }
}