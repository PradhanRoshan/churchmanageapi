package com.chms.churchmanageapi.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "fund_type")
@NamedQuery(name = "FundType.findAll", query = "SELECT f FROM FundType f")
public class FundType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fund_typeid", unique = true, nullable = false)
    private long fundTypeid;

    @Column(name = "fund_type_name", length = 255, nullable = false)
    private String fundTypeName;

    @OneToMany(mappedBy = "fundType")
    private List<TitheAndOffering> titheAndOfferings;

    public FundType() {
    }

    // Getters and setters

    public long getFundTypeid() {
        return fundTypeid;
    }

    public void setFundTypeid(long fundTypeid) {
        this.fundTypeid = fundTypeid;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    public List<TitheAndOffering> getTitheAndOfferings() {
        return titheAndOfferings;
    }

    public void setTitheAndOfferings(List<TitheAndOffering> titheAndOfferings) {
        this.titheAndOfferings = titheAndOfferings;
    }
}
