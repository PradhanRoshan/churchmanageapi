package com.chms.churchmanageapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.Address}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto implements Serializable {
    private long idAddr;
    private String aptNo;
    private String city;
    private String state;
    private String street;
    private String zip;
    @JsonFormat(pattern="MM-dd-yyyy", timezone="America/New_York")
    private Date addrExptn;
}