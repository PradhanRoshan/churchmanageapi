package com.chms.churchmanageapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable implements Serializable {

    private static final long serialVersionUID =1L;

    @Column(name = "DTTM_CREATE", updatable = false)
    @CreationTimestamp
    private Timestamp dttmCreate;

    @Column(name = "DTTM_LST_UPDT", updatable = false)
    @CreationTimestamp
    private  Timestamp dttmLstUpdt;

    @Column(name = "ID_USER_CREATE", updatable = false)
    @CreatedBy
    private String idUserCreate;

    @Column(name = "ID_USER_LST_UPDT")
    @LastModifiedBy
    private String idUserLstUpdt;

    public Timestamp getDttmCreate() {
        return dttmCreate;
    }

    public void setDttmCreate(Timestamp dttmCreate) {
        this.dttmCreate = dttmCreate;
    }

    public Timestamp getDttmLstUpdt() {
        return dttmLstUpdt;
    }

    public void setDttmLstUpdt(Timestamp dttmLstUpdt) {
        this.dttmLstUpdt = dttmLstUpdt;
    }

    public String getIdUserCreate() {
        return idUserCreate;
    }

    public void setIdUserCreate(String idUserCreate) {
        this.idUserCreate = idUserCreate;
    }

    public String getIdUserLstUpdt() {
        return idUserLstUpdt;
    }

    public void setIdUserLstUpdt(String idUserLstUpdt) {
        this.idUserLstUpdt = idUserLstUpdt;
    }
}
