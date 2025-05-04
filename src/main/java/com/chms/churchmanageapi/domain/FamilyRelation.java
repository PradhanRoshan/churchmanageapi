package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "family_relations")
@NamedQuery(name = "FamilyRelation.findAll", query = "SELECT f FROM FamilyRelation f")
public class FamilyRelation extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", unique = true, nullable = false)
    private long relationId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "related_member_id", nullable = false)
    private Member relatedMember;

    @Column(name = "relationship", length = 255, nullable = false)
    private String relationship;

    @Temporal(TemporalType.DATE)
    @Column(name = "fam_rel_exptn", nullable = true)
    private Date famRelExptn;

    public FamilyRelation() {
    }

    // Getters and setters

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Member getRelatedMember() {
        return relatedMember;
    }

    public void setRelatedMember(Member relatedMember) {
        this.relatedMember = relatedMember;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Date getFamRelExptn() {
        return famRelExptn;
    }

    public void setFamRelExptn(Date famRelExptn) {
        this.famRelExptn = famRelExptn;
    }
}
