package com.chms.churchmanageapi.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.FundType}
 */
@Value
public class FundTypeDTO implements Serializable {
    private long fundTypeId;
    private String fundTypeName;
}