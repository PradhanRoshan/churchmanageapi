package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.domain.Address;
import com.chms.churchmanageapi.domain.Member;
import com.chms.churchmanageapi.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    String registerUser(SignUpDTO signUpDTO);

    MemberDto getMemberDtoDetails(Member member);

    RoleDto getRoleDtoDetails(long userId);

    AddressDto getAddressDtoDetails(Address address);

    ResponseEntity<LoginResponseDTO> userLoginAuthentication(AuthRequestDTO authRequest);

    String resetUserPassword(ResetPasswordDTO resetPasswordDTO);

    public void logApplicationStatus(Member member, String applicationType, String comment);

    UserDetialsDto getUserDetails(String memberID);
}
