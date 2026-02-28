package com.chms.churchmanageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.Member}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationTrackingDTO implements Serializable {
    private static final long serialVersionUID = 7769826370850048167L;
    private  MemberDto userMember;
    private RoleDto role;
    private  AddressDto address;
    private  ApplicationStatusDto applicationStatus;
    private List<RgstrnRqstCmntDTO> comments;
}