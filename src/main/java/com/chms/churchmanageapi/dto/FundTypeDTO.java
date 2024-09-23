package com.chms.churchmanageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.FundType}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundTypeDTO implements Serializable {
    private long fundTypeId;
    private String fundTypeName;
}