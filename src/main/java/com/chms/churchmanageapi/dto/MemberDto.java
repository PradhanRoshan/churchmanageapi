package com.chms.churchmanageapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.Member}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto implements Serializable {
    private static final long serialVersionUID = -6641246811784863518L;
    private String memberId;
    private String emailId;
    private String firstName;
    private String gender;
    private String lastName;
    private String maritalStatus;
    private String phoneNumber;
    @JsonFormat(pattern="MM-dd-yyyy", timezone="America/New_York")
    private Timestamp dttmCreate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/New_York")
    private Date memberDob;
    private String status;
    private String applicationSts;
    private String middleName;

}