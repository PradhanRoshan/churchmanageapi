package com.chms.churchmanageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.Member}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileDTO implements Serializable {
    private static final long serialVersionUID = -2498002392857878036L;
    private  MemberDto member;
    private  AddressDto address;
}