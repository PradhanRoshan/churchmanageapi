package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "rgstrn_rqst_cmnt")
@NamedQuery(name = "RgstrnRqstCmnt.findAll", query = "SELECT c FROM RgstrnRqstCmnt c")
public class RgstrnRqstCmnt extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rgstrn_rqst_cmnt_id", nullable = false)
    private Long id;

    @Size(max = 8)
    @NotNull
    @Column(name = "member_id", nullable = false, length = 8)
    private String memberId;

    @Size(max = 255)
    @NotNull
    @Column(name = "rgstrn_rqst_cmnt_role", nullable = false)
    private String rgstrnRqstCmntRole;

    @NotNull
    @Column(name = "text_rgstrn_rqst_cmnt", nullable = false, length = Integer.MAX_VALUE)
    private String textRgstrnRqstCmnt;

    @Size(max = 255)
    @NotNull
    @Column(name = "name_rgstrn_rqst_cmnt_user", nullable = false)
    private String nameRgstrnRqstCmntUser;

    public RgstrnRqstCmnt(Long id, String memberId, String rgstrnRqstCmntRole, String textRgstrnRqstCmnt, String nameRgstrnRqstCmntUser) {
        this.id = id;
        this.memberId = memberId;
        this.rgstrnRqstCmntRole = rgstrnRqstCmntRole;
        this.textRgstrnRqstCmnt = textRgstrnRqstCmnt;
        this.nameRgstrnRqstCmntUser = nameRgstrnRqstCmntUser;
    }

    public RgstrnRqstCmnt() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRgstrnRqstCmntRole() {
        return rgstrnRqstCmntRole;
    }

    public void setRgstrnRqstCmntRole(String rgstrnRqstCmntRole) {
        this.rgstrnRqstCmntRole = rgstrnRqstCmntRole;
    }

    public String getTextRgstrnRqstCmnt() {
        return textRgstrnRqstCmnt;
    }

    public void setTextRgstrnRqstCmnt(String textRgstrnRqstCmnt) {
        this.textRgstrnRqstCmnt = textRgstrnRqstCmnt;
    }

    public String getNameRgstrnRqstCmntUser() {
        return nameRgstrnRqstCmntUser;
    }

    public void setNameRgstrnRqstCmntUser(String nameRgstrnRqstCmntUser) {
        this.nameRgstrnRqstCmntUser = nameRgstrnRqstCmntUser;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RgstrnRqstCmnt that = (RgstrnRqstCmnt) o;
        return Objects.equals(id, that.id) && Objects.equals(memberId, that.memberId) && Objects.equals(rgstrnRqstCmntRole, that.rgstrnRqstCmntRole) && Objects.equals(textRgstrnRqstCmnt, that.textRgstrnRqstCmnt) && Objects.equals(nameRgstrnRqstCmntUser, that.nameRgstrnRqstCmntUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, rgstrnRqstCmntRole, textRgstrnRqstCmnt, nameRgstrnRqstCmntUser);
    }

    @Override
    public String toString() {
        return "RgstrnRqstCmnt{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", rgstrnRqstCmntRole='" + rgstrnRqstCmntRole + '\'' +
                ", textRgstrnRqstCmnt='" + textRgstrnRqstCmnt + '\'' +
                ", nameRgstrnRqstCmntUser='" + nameRgstrnRqstCmntUser + '\'' +
                '}';
    }

}