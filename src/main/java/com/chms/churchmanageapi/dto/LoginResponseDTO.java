package com.chms.churchmanageapi.dto;

import com.chms.churchmanageapi.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResponseDTO {
    private String token;
    private long userRole;
    private long expiresIn;
    private UserDetialsDto userDetialsDto;

}
