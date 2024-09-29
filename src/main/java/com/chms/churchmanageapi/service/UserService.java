package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.dto.AuthRequestDTO;
import com.chms.churchmanageapi.dto.LoginResponseDTO;
import com.chms.churchmanageapi.dto.ResetPasswordDTO;
import com.chms.churchmanageapi.dto.SignUpDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String registerUser(SignUpDTO signUpDTO);

    ResponseEntity<LoginResponseDTO> userLoginAuthentication(AuthRequestDTO authRequest);

    String resetUserPassword(ResetPasswordDTO resetPasswordDTO);
}
