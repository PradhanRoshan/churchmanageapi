package com.chms.churchmanageapi.dto;

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
public class UserDetialsDto implements Serializable {
    private static final long serialVersionUID = -5975937507795382872L;
    private UserDto user;
    private RoleDto role;
    private MemberDto member;
    private AddressDto address;
}