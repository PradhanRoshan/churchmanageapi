package com.chms.churchmanageapi.dto;

import com.chms.churchmanageapi.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long userRole;
    private long expiresIn;
    private UserDetialsDto userDetialsDto;

    public String getToken() {
        return token;
    }
}
